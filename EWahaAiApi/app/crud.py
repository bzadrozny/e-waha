from sqlalchemy.orm import Session

from . import models, schemas


def update_station(db: Session, station: schemas.Station):
    db_station = db.query(models.Station).filter(models.Station.localization == station.localization).first()
    if db_station is None:
        db_station = models.Station(localization=station.localization, prices=station.prices)
        db.add(db_station)
    else:
        db_station.prices = station.prices
    db.commit()
