from __future__ import annotations

import asyncio
import json
import re
from typing import Any

import google.generativeai as genai

from app.config import settings


def _configured_model(model_name: str = "gemini-1.5-flash") -> genai.GenerativeModel:
    """Return a configured GenerativeModel. Raises RuntimeError if key is missing."""
    key = settings.GEMINI_API_KEY
    if not key:
        raise RuntimeError(
            "GEMINI_API_KEY environment variable is not set. "
            "Add it to your deployment secrets."
        )
    genai.configure(api_key=key)
    return genai.GenerativeModel(model_name)


IMAGE_ANALYSIS_PROMPT = """
You are a satirical AI analyzing a photo for an awareness app against dowry culture.

Analyze this person's photo and generate ONLY a JSON response:

{
  "pose_analysis": "funny satirical 1-line description of pose",
  "fashion_vibe": "satirical fashion commentary",
  "detected_aura": "government_job / software_engineer / dubai_return / startup_bro / etc",
  "premium_rishta_rating": 7.5,
  "fake_ego_level": 8,
  "ai_roast_line": "one funny satirical roast line in Hinglish",
  "floating_tags": ["Tag1", "Tag2", "Tag3"],
  "meme_caption": "meme-style caption"
}

IMPORTANT:
- Keep it SATIRICAL and FUNNY, NOT offensive
- Target the CONCEPT of dowry, not the person
- Use Hinglish (Hindi + English mix)
- Keep it family-friendly satire
- Respond ONLY with valid JSON, no other text
"""


def _occupation_satire_prompt(
    occupation: str, salary: int, ego_level: int, abroad_status: str
) -> str:
    return f"""
You are a Dakni Urdu satire writer for an awareness app against dowry culture.

Generate satire for: {occupation} | Salary: ₹{salary}/month | Ego: {ego_level}/10 | Location: {abroad_status}

Return ONLY valid JSON:
{{
  "dakni_urdu_roast": "funny roast in Dakni Urdu (2-3 lines)",
  "hindi_satire": "Hindi satire line",
  "english_roast": "English sarcastic one-liner",
  "occupation_badge": "funny badge name like 'Government Maharaj' or 'Software Sahab'",
  "dowry_multiplier": 3.5,
  "ego_commentary": "funny comment on ego level",
  "family_pressure_line": "funny line about family expectations",
  "rishta_premium_score": 8.2,
  "meme_summary": "meme-style summary line"
}}

Examples:
- IAS: "Dakni: Sarkari thane ka darwaza khulte hi Fortuner aur 50 tola sona ki demand shuru!"
- Software Engineer: "US dreams detect hua, H1B visa ke saath teen BHK ki demand bhi aai!"
- Dubai Return: "Dubai se aate hi ego level 100 pe, demand bhi Dubai market rate pe!"

IMPORTANT: Satirical, funny, family-friendly. Target dowry culture, not the person.
Return ONLY valid JSON.
"""


def _dowry_calculation_prompt(user_data: dict[str, Any]) -> str:
    return f"""
You are a satirical dowry calculator for an awareness app.

Input data:
- Occupation: {user_data['occupation']}
- Salary: ₹{user_data['salary']}/month
- Abroad: {user_data['abroad_status']}
- Ego Level: {user_data['ego_level']}/10
- Family Expectation: {user_data['family_expectation']}/10
- Luxury Level: {user_data['luxury_level']}/10
- Gold Demand: {user_data['gold_kg']} kg
- Car Demand: {user_data['car']}
- Wedding Level: {user_data['wedding_level']}

Generate satirical (fictional/fake) dowry invoice as JSON:
{{
  "total_fake_amount": 5000000,
  "line_items": [
    {{"item": "Fortuner", "amount": 4200000, "emoji": "🚗", "satire_note": "funny note"}},
    {{"item": "Gold 10kg", "amount": 6800000, "emoji": "💍", "satire_note": "funny note"}},
    {{"item": "2BHK Flat", "amount": 8500000, "emoji": "🏠", "satire_note": "funny note"}},
    {{"item": "iPhone Package", "amount": 200000, "emoji": "📱", "satire_note": "funny note"}},
    {{"item": "AC+Fridge+TV", "amount": 300000, "emoji": "❄️", "satire_note": "funny note"}},
    {{"item": "Wedding Shaadi", "amount": 2000000, "emoji": "💒", "satire_note": "funny note"}}
  ],
  "satire_disclaimer": "funny disclaimer text",
  "invoice_header": "JAHAIZ KA OFFICIAL HISAAB 📋",
  "footer_joke": "funny invoice footer"
}}

Make amounts exaggerated and satirical. Return ONLY valid JSON.
"""


def _strip_markdown_json(text: str) -> str:
    t = text.strip()
    if t.startswith("```"):
        parts = t.split("```")
        if len(parts) >= 2:
            t = parts[1]
            if t.lstrip().startswith("json"):
                t = re.sub(r"^json\s*", "", t.lstrip(), flags=re.I)
    return t.strip()


def _parse_json_loose(text: str) -> dict[str, Any]:
    return json.loads(_strip_markdown_json(text))


async def analyze_image(image_base64: str, mime_type: str = "image/jpeg") -> dict[str, Any]:
    def _sync() -> dict[str, Any]:
        model = _configured_model()
        prompt = IMAGE_ANALYSIS_PROMPT
        mt = mime_type if mime_type in ("image/jpeg", "image/png", "image/webp") else "image/jpeg"
        image_part = {"inline_data": {"mime_type": mt, "data": image_base64}}
        response = model.generate_content([prompt, image_part])
        txt = getattr(response, "text", None) or ""
        return _parse_json_loose(txt)

    return await asyncio.to_thread(_sync)


async def generate_satire(
    occupation: str, salary: int, ego_level: int, abroad_status: str
) -> dict[str, Any]:
    def _sync() -> dict[str, Any]:
        model = _configured_model()
        prompt = _occupation_satire_prompt(occupation, salary, ego_level, abroad_status)
        response = model.generate_content(prompt)
        txt = getattr(response, "text", "") or ""
        return _parse_json_loose(txt)

    return await asyncio.to_thread(_sync)


async def calculate_dowry_satire(user_data: dict[str, Any]) -> dict[str, Any]:
    def _sync() -> dict[str, Any]:
        model = _configured_model()
        prompt = _dowry_calculation_prompt(user_data)
        response = model.generate_content(prompt)
        txt = getattr(response, "text", "") or ""
        return _parse_json_loose(txt)

    return await asyncio.to_thread(_sync)
