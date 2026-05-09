from pydantic import BaseModel
from typing import Any


class HealthResponse(BaseModel):
    status: str


class ApiMessage(BaseModel):
    message: str
    detail: Any | None = None
