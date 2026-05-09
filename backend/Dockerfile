# Python 3.11 — avoids native runtime defaulting to 3.14+ and Pillow sdist failures.
FROM python:3.11.11-slim-bookworm

WORKDIR /app

ENV PYTHONUNBUFFERED=1 \
    PIP_NO_CACHE_DIR=1 \
    PIP_DISABLE_PIP_VERSION_CHECK=1

COPY requirements.txt .
# region agent log
RUN python -c "import json,sys; print(json.dumps({'sessionId':'c0007f','hypothesisId':'H-docker','message':'docker-build-python','data':{'version':sys.version.split()[0]},'timestamp':__import__('time').time_ns()//1000000}), flush=True)"
# endregion agent log
RUN pip install --no-cache-dir -r requirements.txt

COPY app ./app

EXPOSE 8000

CMD ["sh", "-c", "uvicorn app.main:app --host 0.0.0.0 --port ${PORT:-8000}"]
