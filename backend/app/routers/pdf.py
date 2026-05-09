from __future__ import annotations

from datetime import datetime, timezone

from fastapi import APIRouter, HTTPException, Response

from app.models.request_models import PdfGenerateRequest
from app.services.pdf_service import generate_branded_pdf

router = APIRouter()


@router.post("/generate")
async def generate_pdf(body: PdfGenerateRequest):
    try:
        raw = generate_branded_pdf(
            body.name,
            body.photo_base64,
            body.photo_mime or "image/jpeg",
            body.image_analysis,
            body.occupation_satire,
            body.dowry_invoice,
        )
        ts = datetime.now(tz=timezone.utc).strftime("%Y%m%d_%H%M%S")
        filename = f"jahaiz_hisaab_{safe_filename(body.name)}_{ts}"
        return Response(
            content=raw,
            media_type="application/pdf",
            headers={"Content-Disposition": f'attachment; filename="{filename}.pdf"'},
        )
    except Exception as exc:  # noqa: BLE001
        raise HTTPException(status_code=500, detail=str(exc)) from exc


def safe_filename(name: str) -> str:
    cleaned = "".join(c for c in (name or "guest") if c.isalnum() or c in (" ", "-", "_")).strip()
    cleaned = cleaned.replace(" ", "_") or "guest"
    return f"jahaiz_hisaab_{cleaned}"
