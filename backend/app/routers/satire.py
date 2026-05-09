from __future__ import annotations

from fastapi import APIRouter, HTTPException

from app.models.request_models import OccupationSatireRequest
from app.services import satire_engine

router = APIRouter()


@router.post("/generate")
async def generate_occupation_satire(payload: OccupationSatireRequest):
    try:
        return await satire_engine.occupation_satire(
            payload.occupation,
            payload.salary,
            payload.ego_level,
            payload.abroad_status,
        )
    except Exception as exc:  # noqa: BLE001
        raise HTTPException(status_code=500, detail=str(exc)) from exc
