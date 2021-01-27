import asyncio
import logging
import re

from typing import List

from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes, Line
from fastapi import BackgroundTasks, FastAPI
from msrest.authentication import CognitiveServicesCredentials

from . import crud, schemas
from .database import SessionLocal

subscription_key = "39357b91fe6e48e59f842ae2e10aaa80"
endpoint = "https://ewahavision.cognitiveservices.azure.com/"
price_regex = re.compile(r'\d[.,]\d\d')

fuel_type_regexes = {
    "ON": re.compile(r'[0O]N|[Dd]iesel'),
    "LPG": re.compile(r'gaz|lpg', re.IGNORECASE),
    "98": re.compile(r'[^,.]?98'),
    "95": re.compile(r'[^,.]?95')
}

logger = logging.getLogger("uvicorn.access")
computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))
app = FastAPI()

app.on_event("shutdown")
def shutdown():
    computervision_client.close()

async def process_lines(lines: List[Line]):
    i = 0
    prices = {}
    while i < len(lines) - 1:
        line = lines[i].text
        for fuel_type in fuel_type_regexes.keys():
            if re.search(fuel_type_regexes[fuel_type], line):
                price = re.search(price_regex, line)
                if price is None:
                    price = re.search(price_regex, lines[i+1].text)
                if price is None:
                    continue
                price = float(price.group().replace(',','.'))
                if fuel_type not in prices or prices[fuel_type] > price:
                    prices[fuel_type] = price
                    logger.info("Found price {}:{}".format(fuel_type, price))
        i += 1
    return prices


async def process_image(loc: str, url: str):
    db = SessionLocal()
    recognize_results = computervision_client.read(url, raw=True)
    operation_location_remote = recognize_results.headers["Operation-Location"]
    # Grab the ID from the URL
    operation_id = operation_location_remote.split("/")[-1]

    # Call the "GET" API and wait for it to retrieve the results
    while True:
        get_handw_text_results = computervision_client.get_read_result(operation_id)
        if get_handw_text_results.status not in ['notStarted', 'running']:
            break
        await asyncio.sleep(1)
    # Print the detected text, line by line
    if get_handw_text_results.status != OperationStatusCodes.succeeded:
        return

    prices = None

    for text_result in get_handw_text_results.analyze_result.read_results:
        prices = await process_lines(text_result.lines)

    if prices is None or len(prices) == 0:
        return

    station = schemas.Station(localization=loc, prices=str(prices))
    crud.update_station(db=db, station=station)
    db.close()

@app.post("/")
async def process_photo(loc: str, photo_url: str, background_tasks: BackgroundTasks):
    background_tasks.add_task(process_image, loc, photo_url)


