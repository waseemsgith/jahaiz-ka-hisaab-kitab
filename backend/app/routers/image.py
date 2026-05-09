from __future__ import annotations

import base64

from fastapi import APIRouter, File, HTTPException, UploadFile

from app.models.request_models import ImageAnalyzeJsonRequest
from app.services import satire_engine

router = APIRouter()


@router.post("/analyze")
async def analyze_json(body: ImageAnalyzeJsonRequest):
    try:
        data = await satire_engine.image_analysis_roast(body.image_base64.strip(), body.mime_type)
        return data
    except Exception as exc:  # noqa: BLE001
        raise HTTPException(status_code=500, detail=str(exc)) from exc


@router.post("/analyze-upload")
async def analyze_upload(file: UploadFile = File(...)):
    try:
        raw = await file.read()
        if not raw:
            raise HTTPException(status_code=400, detail="Empty file")
        b64 = base64.b64encode(raw).decode("ascii")
        mime = file.content_type or "image/jpeg"
        data = await satire_engine.image_analysis_roast(b64, mime_type=mime if mime.startswith("image/") else "image/jpeg")
        return data
    except HTTPException:
        raise
    except Exception as exc:  # noqa: BLE001
        raise HTTPException(status_code=500, detail=str(exc)) from exc
