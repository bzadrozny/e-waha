FROM tiangolo/uvicorn-gunicorn-fastapi:latest

COPY pyproject.toml poetry.lock* /

RUN \
    curl -sSL https://raw.githubusercontent.com/python-poetry/poetry/master/get-poetry.py | POETRY_HOME=/opt/poetry python && \
    cd /usr/local/bin && \
    ln -s /opt/poetry/bin/poetry && \
    poetry config virtualenvs.create false && \
    poetry install --no-root --no-dev

COPY ./app /app/app