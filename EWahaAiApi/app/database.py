from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

SQLALCHEMY_DATABASE_URL = "postgresql://notanadmin@ewahapostgresql:TestPassword123@ewahapostgresql.postgres.database.azure.com/demo"

engine = create_engine(SQLALCHEMY_DATABASE_URL, pool_size=10)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()
