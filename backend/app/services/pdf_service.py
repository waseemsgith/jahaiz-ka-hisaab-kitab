"""Brand PDF export with dark/gold meme receipt styling.

Pillow is an optional dependency — if not installed the photo is skipped
but all text content (satire, invoice, roast) is still included.
"""

from __future__ import annotations

import base64
import io
from datetime import datetime

from reportlab.lib import colors
from reportlab.lib.pagesizes import letter
from reportlab.lib.units import inch
from reportlab.pdfgen import canvas

# Pillow is optional — import lazily so startup never fails.
try:
    from PIL import Image as PILImage  # noqa: F401
    _PILLOW_AVAILABLE = True
except ModuleNotFoundError:
    _PILLOW_AVAILABLE = False


FOOTER_LEFT = "Built by Waseem Shareef K S | جہیز کا حساب کتاب"


def generate_branded_pdf(
    name: str,
    photo_base64: str | None,
    photo_mime: str | None,
    image_analysis: dict,
    occupation_satire: dict,
    dowry_invoice: dict,
) -> bytes:
    buf = io.BytesIO()
    c = canvas.Canvas(buf, pagesize=letter)
    w, h = letter

    bg = colors.HexColor("#0A0A0A")
    c.setFillColor(bg)
    c.rect(0, 0, w, h, stroke=0, fill=1)

    gold = colors.HexColor("#FFD700")
    c.setStrokeColor(colors.HexColor("#B8860B"))
    c.setFillColor(colors.HexColor("#1A1A1A"))
    c.roundRect(36, h - 110, w - 72, 74, 10, stroke=1, fill=1)
    c.setFillColor(gold)
    c.setFont("Helvetica-Bold", 18)
    c.drawCentredString(w / 2, h - 68, "حساب کتاب — JAHAIZ KA HISAAB KITAB")
    c.setFont("Helvetica", 10)
    c.setFillColor(colors.white)
    stamped = datetime.utcnow().strftime("%Y-%m-%d %H:%M UTC")
    c.drawString(48, h - 94, f"Guest / Name field: {name}   |   {stamped}")

    y = h - 130
    c.setFillColor(colors.HexColor("#FFFFFF"))

    img_drawn_y = None
    if photo_base64 and _PILLOW_AVAILABLE:
        try:
            from PIL import Image as _PIL
            raw = base64.b64decode(photo_base64, validate=False)
            pil_img = _PIL.open(io.BytesIO(raw))
            pil_rgb = pil_img.convert("RGB")
            img_side = min(240, pil_rgb.width)
            img_buf = io.BytesIO()
            pil_rgb.thumbnail((img_side, img_side))
            pil_rgb.save(img_buf, format="JPEG")
            img_buf.seek(0)
            iw, ih = pil_rgb.width, pil_rgb.height
            max_w = 2.6 * inch
            scale = min(max_w / float(iw), max_w / float(ih))
            dw = iw * scale
            dh = ih * scale
            x = (w - dw) / 2
            c.drawInlineImage(io.BytesIO(img_buf.getvalue()), x, y - dh - 10, dw, dh)
            img_drawn_y = y - dh - 30
        except Exception:
            img_drawn_y = None

    y_cursor = img_drawn_y if img_drawn_y is not None else y

    c.setStrokeColor(colors.HexColor("#33FFD700"))
    c.rotate(38)
    c.setFillColor(colors.Color(1, 0.84, 0, alpha=0.08))
    c.setFont("Helvetica-Bold", 64)
    c.drawString(120, -80, "")
    c.rotate(-38)

    def draw_block(title: str, content: list[str]):
        nonlocal y_cursor
        c.setFillColor(gold)
        c.setFont("Helvetica-Bold", 12)
        c.drawString(40, y_cursor, title)
        y_cursor -= 18
        c.setFillColor(colors.lightgrey)
        c.setFont("Helvetica", 9)
        for line in content:
            if y_cursor < 90:
                c.showPage()
                c.setFillColor(bg)
                c.rect(0, 0, w, h, stroke=0, fill=1)
                y_cursor = h - 60
            wrapped = wrap_text(line, 95)
            for wl in wrapped:
                c.drawString(48, y_cursor, wl)
                y_cursor -= 12

    tags = image_analysis.get("floating_tags") or []
    roast = [
        image_analysis.get("pose_analysis", ""),
        image_analysis.get("fashion_vibe", ""),
        image_analysis.get("ai_roast_line", ""),
        f"Detected aura (satire tag): {image_analysis.get('detected_aura', '')}",
        f"Premium rishta rating: {image_analysis.get('premium_rishta_rating', '')}",
        f"Floating tags: {', '.join(tags)}",
        image_analysis.get("meme_caption", ""),
    ]
    draw_block("AI Image Roast (satire)", roast)

    occ = [
        str(occupation_satire.get("occupation_badge", "")),
        str(occupation_satire.get("dakni_urdu_roast", "")),
        str(occupation_satire.get("hindi_satire", "")),
        str(occupation_satire.get("english_roast", "")),
        str(occupation_satire.get("meme_summary", "")),
        f"Dowry multiplier (meme metric): {occupation_satire.get('dowry_multiplier', '')}",
        f"Rishta premium score: {occupation_satire.get('rishta_premium_score', '')}",
    ]
    draw_block("Occupation Satire", occ)

    inv_lines = dowry_invoice.get("line_items") or []
    inv_text = [
        str(dowry_invoice.get("invoice_header", "")),
        f"Amt total (fiction): ₹{int(dowry_invoice.get('total_fake_amount', 0)):,}",
    ]
    for it in inv_lines:
        inv_text.append(
            f"{it.get('emoji','')} {it.get('item','')} — ₹{int(it.get('amount',0)):,} ({it.get('satire_note','')})"
        )
    inv_text.append(str(dowry_invoice.get("satire_disclaimer", "")))
    inv_text.append(str(dowry_invoice.get("footer_joke", "")))
    draw_block("Fake Dowry Invoice (awareness meme)", inv_text)

    c.setStrokeColor(colors.HexColor("#33FFD700"))
    c.rect(44, y_cursor - 6, w - 88, max(220, len(inv_text) * 10), stroke=1)

    c.saveState()
    c.translate(w - inch * 2.2, inch * 9.5)
    c.rotate(32)
    c.setFillColor(colors.Color(1, 0.84, 0, alpha=0.07))
    c.setFont("Helvetica-Bold", 44)
    c.drawString(0, 0, "جہیز کا حساب کتاب")
    c.restoreState()

    footer_y = 36
    c.setFillColor(colors.HexColor("#888888"))
    c.setFont("Helvetica", 8)
    c.drawCentredString(w / 2, footer_y, FOOTER_LEFT)

    c.save()
    return buf.getvalue()


def wrap_text(text: str, width: int) -> list[str]:
    words = (text or "").split()
    if not words:
        return [""]
    lines = []
    cur = words[0]
    for word in words[1:]:
        if len(cur) + 1 + len(word) <= width:
            cur += " " + word
        else:
            lines.append(cur)
            cur = word
    lines.append(cur)
    return lines
