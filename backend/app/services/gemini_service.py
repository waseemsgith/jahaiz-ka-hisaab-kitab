from __future__ import annotations

import asyncio
import json
import re
import random
from typing import Any

import google.generativeai as genai
from google.generativeai.types import GenerationConfig

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


SATIRE_MODES = [
    "Dakni Roast", "Meme Reporter", "Family WhatsApp Group",
    "Rishta Broker Commentary", "Bollywood Narrator", "Fake News Anchor",
    "LinkedIn Corporate Roast", "Hyderabadi Chai Adda Roast",
    "Shaadi Market Analyst", "Toxic Rishtedaar Commentary"
]

TONES = [
    "highly sarcastic", "dramatically shocked", "passively aggressive",
    "over-enthusiastic broker", "disappointed uncle", "meme lord",
    "gossiping aunty", "fake intellectual"
]

MEME_REFERENCES = [
    "Hera Pheri style", "Mirzapur style", "Shark Tank pitch",
    "Big Boss drama", "Taarak Mehta reaction", "Generic Instagram reel trend"
]

RANDOM_OPENINGS = [
    "Breaking news from shaadi market 📺",
    "Hyderabad rishta exchange activated ☕",
    "Government aura levels rising 📈",
    "Fortuner expectation engine started 🚗",
    "LinkedIn premium rishta detected 💼",
    "NRI package inflation scanner online ✈️",
    "Dowry demand scaling factors checking... 📈"
]

RANDOM_ENDINGS = [
    "Rishta market unstable 😭",
    "Middle class under pressure 📉",
    "Family expectations exceeded 💸",
    "Shaadi inflation continues 📊",
    "Society ego meter exploded 🧨"
]

OCCUPATION_ROAST_THEMES = {
    "SOFTWARE_ENGINEER": [
        "package flex roast", "remote work roast", "startup burnout roast",
        "laptop lifestyle roast", "LinkedIn cringe roast", "Leetcode survivor roast"
    ],
    "GOVERNMENT_JOB": [
        "government aura roast", "VIP culture roast", "rishta premium activated roast",
        "collector sahab satire", "pension flex joke"
    ],
    "DOCTOR": [
        "clinic inheritance roast", "MD/MS degree flex", "24/7 duty but high dowry roast"
    ],
    "BUSINESSMAN": [
        "papa ka business flex", "tax evasion joke", "Fortuner standard roast", "showroom owner ego"
    ],
    "DUBAI_RETURN": [
        "dollar/dirham conversion satire", "Dubai aura roast", "foreign return inflation roast",
        "perfume and iPhone flex"
    ],
    "NRI": [
        "green card bait roast", "US dreams detect hua", "H1B visa flex", "fake accent roast"
    ],
    "DEFAULT": [
        "middle-class struggle roast", "unrealistic family expectation", "general shaadi market roast"
    ]
}


def _get_random_config() -> tuple[str, str, str, str, str]:
    return (
        random.choice(SATIRE_MODES),
        random.choice(TONES),
        random.choice(MEME_REFERENCES),
        random.choice(RANDOM_OPENINGS),
        random.choice(RANDOM_ENDINGS)
    )

def _get_occupation_theme(occupation: str) -> str:
    # Attempt to match generic terms or keys
    occ_upper = occupation.upper()
    theme_list = OCCUPATION_ROAST_THEMES.get("DEFAULT")
    for key, themes in OCCUPATION_ROAST_THEMES.items():
        if key in occ_upper or occ_upper in key:
            theme_list = themes
            break
    return random.choice(theme_list)


def _image_analysis_prompt() -> str:
    mode, tone, meme, opening, ending = _get_random_config()
    return f"""
You are a satirical AI analyzing a photo for an awareness app against dowry culture.
Today's Mode: {mode} | Tone: {tone} | Meme Reference: {meme}
Random Intro Vibe: {opening}

Analyze this person's photo and generate ONLY a JSON response:

{{
  "pose_analysis": "funny satirical 1-line description of pose",
  "fashion_vibe": "satirical fashion commentary",
  "detected_aura": "government_job / software_engineer / dubai_return / startup_bro / etc",
  "premium_rishta_rating": {round(random.uniform(3.0, 9.9), 1)},
  "fake_ego_level": {random.randint(5, 10)},
  "ai_roast_line": "one funny satirical roast line in Hinglish that feels completely fresh",
  "floating_tags": ["Tag1", "Tag2", "Tag3"],
  "meme_caption": "meme-style caption"
}}

IMPORTANT:
- NEVER repeat previous wording. Generate fresh satire every request.
- Target the CONCEPT of dowry/ego, not the person.
- Use Hinglish (Hindi + English mix) or Dakni if applicable.
- Keep it family-friendly satire.
- Respond ONLY with valid JSON, no other text.
"""


def _occupation_satire_prompt(
    occupation: str, salary: int, ego_level: int, abroad_status: str
) -> str:
    mode, tone, meme, opening, ending = _get_random_config()
    specific_theme = _get_occupation_theme(occupation)
    return f"""
You are a satire writer for an awareness app against dowry culture.
Current Style: {mode} | Tone: {tone} | Inspiration: {meme}
Mandatory Sub-theme for this occupation: {specific_theme}
Random Intro: {opening}
Random Ending vibe: {ending}

Generate fresh, completely unique satire for: {occupation} | Salary: ₹{salary}/month | Ego: {ego_level}/10 | Location: {abroad_status}

CRITICAL: NEVER repeat previous wording. Use different tone and structure each time.

Return ONLY valid JSON:
{{
  "dakni_urdu_roast": "funny roast in Dakni Urdu (2-3 lines, highly unique and unpredictable based on the sub-theme '{specific_theme}')",
  "hindi_satire": "Hindi satire line (unpredictable)",
  "english_roast": "English sarcastic one-liner (very fresh)",
  "occupation_badge": "funny fresh badge name like 'Government Maharaj' or 'Software Sahab'",
  "dowry_multiplier": {round(random.uniform(1.5, 5.5), 1)},
  "ego_commentary": "funny comment on their {ego_level}/10 ego level",
  "family_pressure_line": "funny line about family expectations",
  "rishta_premium_score": {round(random.uniform(4.0, 9.5), 1)},
  "meme_summary": "meme-style summary line"
}}

IMPORTANT: Satirical, funny, family-friendly. Target dowry culture.
Return ONLY valid JSON.
"""


def _dowry_calculation_prompt(user_data: dict[str, Any]) -> str:
    mode, tone, meme, opening, ending = _get_random_config()
    return f"""
You are an intelligent, socially-aware satirical dowry calculator for an Indian awareness app.
Current Persona: {mode} | Tone: {tone} | Vibe: {meme}
Random Intro Vibe: {opening}
Random Ending Vibe: {ending}

Input Profile:
- Occupation: {user_data.get('occupation', 'Unknown')}
- Salary: ₹{user_data.get('salary', 0)}/month
- Abroad: {user_data.get('abroad_status', 'No')}
- Ego Level: {user_data.get('ego_level', 5)}/10
- Family Expectation: {user_data.get('family_expectation', 5)}/10
- Luxury Level: {user_data.get('luxury_level', 5)}/10
- Gold Demand: {user_data.get('gold_kg', 0)} kg
- Car Demand: {user_data.get('car', 'None')}
- Wedding Level: {user_data.get('wedding_level', 'Basic')}

CRITICAL REALISM & SATIRE RULES:
1. Generate realistic, believable amounts based on the Indian social class of the profile:
   - LOWER MIDDLE CLASS: Bike, 5–15 tola gold, furniture, ₹2L–₹8L total pressure.
   - MIDDLE CLASS: Swift/Baleno/Brezza, ₹5L–₹20L expectations, standard wedding expenses.
   - UPPER MIDDLE CLASS: Creta/XUV700/Fortuner, ₹15L–₹60L expectations, luxury wedding.
   - ELITE / IAS / NRI: Exaggerated but believable. ₹50L–₹1.5Cr MAX. NEVER generate absurd 10+ crore amounts.
2. Smart Satire Examples:
   - "Software engineer hai toh Creta expectation toh banta hai 🚗"
   - "Government naukri premium automatically activated 🏛️"
   - "Dubai return aura detected ✈️"
3. Avoid cartoonish nonsense. Make it funny but socially believable.
4. Total amount must intelligently reflect the sum of line items.
5. NEVER repeat previous outputs. Every request must be worded uniquely.

Generate a JSON response EXACTLY matching this structure, with YOUR intelligently calculated dynamic numbers:
{{
  "total_fake_amount": 3870000,
  "line_items": [
    {{"item": "Selected Car/Bike", "amount": 1800000, "emoji": "🚗", "satire_note": "highly unique smart note based on occupation"}},
    {{"item": "Gold (X tola)", "amount": 900000, "emoji": "💍", "satire_note": "highly unique note"}},
    {{"item": "Furniture", "amount": 250000, "emoji": "🛋️", "satire_note": "highly unique note"}},
    {{"item": "Electronics", "amount": 120000, "emoji": "📱", "satire_note": "highly unique note"}},
    {{"item": "Wedding", "amount": 1200000, "emoji": "💒", "satire_note": "highly unique note"}}
  ],
  "satire_disclaimer": "Satire based on social dowry culture trends. This app criticizes dowry practices through humor and awareness.",
  "invoice_header": "JAHAIZ KA OFFICIAL HISAAB 📋",
  "footer_joke": "completely unique realistic invoice footer"
}}

Return ONLY valid JSON.
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
        prompt = _image_analysis_prompt()
        mt = mime_type if mime_type in ("image/jpeg", "image/png", "image/webp") else "image/jpeg"
        image_part = {"inline_data": {"mime_type": mt, "data": image_base64}}
        response = model.generate_content(
            [prompt, image_part],
            generation_config=GenerationConfig(temperature=0.98, top_p=0.95)
        )
        txt = getattr(response, "text", None) or ""
        return _parse_json_loose(txt)

    return await asyncio.to_thread(_sync)


async def generate_satire(
    occupation: str, salary: int, ego_level: int, abroad_status: str
) -> dict[str, Any]:
    def _sync() -> dict[str, Any]:
        model = _configured_model()
        prompt = _occupation_satire_prompt(occupation, salary, ego_level, abroad_status)
        response = model.generate_content(
            prompt,
            generation_config=GenerationConfig(temperature=0.98, top_p=0.95)
        )
        txt = getattr(response, "text", "") or ""
        return _parse_json_loose(txt)

    return await asyncio.to_thread(_sync)


async def calculate_dowry_satire(user_data: dict[str, Any]) -> dict[str, Any]:
    def _sync() -> dict[str, Any]:
        model = _configured_model()
        prompt = _dowry_calculation_prompt(user_data)
        response = model.generate_content(
            prompt,
            generation_config=GenerationConfig(temperature=0.98, top_p=0.95)
        )
        txt = getattr(response, "text", "") or ""
        return _parse_json_loose(txt)

    return await asyncio.to_thread(_sync)
