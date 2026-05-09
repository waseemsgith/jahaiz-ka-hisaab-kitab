"""Satire orchestration — wraps Gemini calls with deterministic fallbacks."""

from __future__ import annotations

from typing import Any

from app.services import gemini_service


async def occupation_satire(
    occupation: str, salary: int, ego_level: int, abroad_status: str
) -> dict[str, Any]:
    try:
        return await gemini_service.generate_satire(occupation, salary, ego_level, abroad_status)
    except Exception:
        return fallback_occupation_satire(occupation, salary, ego_level, abroad_status)


async def image_analysis_roast(image_base64: str, mime_type: str = "image/jpeg") -> dict[str, Any]:
    try:
        return await gemini_service.analyze_image(image_base64, mime_type)
    except Exception:
        return fallback_image_analysis()


def fallback_image_analysis() -> dict[str, Any]:
    return {
        "pose_analysis": "Confidence max — LinkedIn headline energy with wedding flex pose.",
        "fashion_vibe": "Neutral kurta + blazer half formal, full rate card vibes.",
        "detected_aura": "premium_rishta_startup_bro",
        "premium_rishta_rating": 7.9,
        "fake_ego_level": 8,
        "ai_roast_line": "Portfolio me humble, sasural negotiation me Nasdaq listing.",
        "floating_tags": [
            "Government Job Glow (fake)",
            "Dubai Return Pose ✈️",
            "Premium Rishta Stocks 📈",
        ],
        "meme_caption": "Bro thinks dowry is a Series A funding round.",
    }


def fallback_occupation_satire(
    occupation: str, salary: int, ego_level: int, abroad_status: str
) -> dict[str, Any]:
    return {
        "dakni_urdu_roast": (
            f"Bhai {occupation} — maashallah thoda ego {ego_level} pe set hai.\n"
            "Dakni tip: Rishta meeting me ppt se pehle ‘expectations slide’ ata hai kabhi?\n"
        ),
        "hindi_satire": (
            "Satire mode: naam occupation ka, checklist dowry ka — dono invoice pe print ho jate hain."
        ),
        "english_roast": (
            "Career milestones are fine; dowry spreadsheets are the punchline we're roasting."
        ),
        "occupation_badge": f"{occupation} — Rate Card Royale",
        "dowry_multiplier": round(2.0 + ego_level / 5.0 + (1 if "gulf" in abroad_status.lower() else 0), 2),
        "ego_commentary": "Ego meter says: negotiator mode unlocked (satire).",
        "family_pressure_line": (
            "'Log kya kahenge' ne inflation se bhi tez growth dekhi hai."
        ),
        "rishta_premium_score": min(
            9.9, 6.5 + ego_level / 10 + min(salary / 500_000, 2)
        ),
        "meme_summary": (
            f"₹{salary:,}/month salary screen pe, dowry myths society pe — humour target set."
        ),
    }
