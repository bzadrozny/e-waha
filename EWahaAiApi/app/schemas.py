from pydantic import BaseModel


class Station(BaseModel):
    localization: str
    prices: str = None

    class Config:
        orm_mode = True


