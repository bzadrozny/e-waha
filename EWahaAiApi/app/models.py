from sqlalchemy import Column, String, Text

from .database import Base


class Station(Base):
    __tablename__ = "stations"

    localization = Column(String(), primary_key=True)
    prices = Column(Text())
