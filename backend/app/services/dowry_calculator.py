"""Dowry satire calculator — Gemini first, exaggerated local fallback."""

from __future__ import annotations

from typing import Any

from app.services import gemini_service


async def calculate_satirical_invoice(payload: dict[str, Any]) -> dict[str, Any]:
    user_data = {
        "occupation": payload.get("occupation", "Other"),
        "salary": int(payload.get("salary", 0)),
        "abroad_status": payload.get("abroad_status", "India"),
        "ego_level": int(payload.get("ego_level", 5)),
        "family_expectation": int(payload.get("family_expectation", 5)),
        "luxury_level": int(payload.get("luxury_level", 5)),
        "gold_kg": float(payload.get("gold_kg", 0)),
        "car": payload.get("car", ""),
        "wedding_level": payload.get("wedding_level", "Medium"),
    }
    try:
        return await gemini_service.calculate_dowry_satire(user_data)
    except Exception:
        return fallback_invoice(user_data)


def fallback_invoice(u: dict[str, Any]) -> dict[str, Any]:
    ego = float(u["ego_level"])
    fam = float(u["family_expectation"])
    lux = float(u["luxury_level"])
    mult = 1 + (ego + fam + lux) / 30
    salary = float(u["salary"])

    gold_amt = int(2_500_000 * mult + float(u["gold_kg"]) * 800_000)
    car_amt = int(3_800_000 * mult)
    flat_amt = int(9_500_000 * mult)
    iphone = int(180_000 * mult)
    furniture = int(320_000 * mult)
    wedding = int(2_200_000 * mult + salary * 0.5)

    line_items = [
        {"item": u["car"] or "Fortuner (meme SKU)", "amount": car_amt, "emoji": "🚗", "satire_note": "SUV invoice bhi Rishta KPI ban gaya"},
        {"item": f'Gold (~{u["gold_kg"]} kg satire meme)', "amount": gold_amt, "emoji": "💍", "satire_note": "Jewellery ya mutual fund — app targets myths"},
        {"item": "Skyline Flat (Bollywood DLC)", "amount": flat_amt, "emoji": "🏠", "satire_note": "Demand list me pincode prestige tax"},
        {"item": "iPhone family pack DLC", "amount": iphone, "emoji": "📱", "satire_note": "Status bar pe signal, sasural bar pe tariff"},
        {"item": "AC + Double-door lore", "amount": furniture, "emoji": "🛋️", "satire_note": "Appliances are characters in this meme invoice"},
        {"item": "Big Fat Wedding Expansion Pack", "amount": wedding, "emoji": "💒", "satire_note": f'Level {u["wedding_level"]} — satire DLC only'},
    ]
    total = sum(x["amount"] for x in line_items)
    return {
        "total_fake_amount": total,
        "line_items": line_items,
        "satire_disclaimer": "Ye invoice fictional hai — dowry mangna illegal aur harmful hai. App sirf jagrukta ke liye.",
        "invoice_header": "JAHAIZ KA OFFICIAL HISAAB 📋",
        "footer_joke": (
            "Subtotal = ego squared (satire constants apply). Taxes: societal pressure (non-refundable)."
        ),
    }
