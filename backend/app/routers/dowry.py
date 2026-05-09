from __future__ import annotations

from fastapi import APIRouter, HTTPException

from app.models.request_models import DowryCalculateRequest
from app.services import dowry_calculator

router = APIRouter()


@router.post("/calculate")
async def calculate_dowry(payload: DowryCalculateRequest):
    try:
        d = payload.model_dump()
        return await dowry_calculator.calculate_satirical_invoice(d)
    except Exception as exc:  # noqa: BLE001
        raise HTTPException(status_code=500, detail=str(exc)) from exc
