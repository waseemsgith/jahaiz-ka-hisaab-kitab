# Jahaiz Ka Hisaab Kitab (جہیز کا حساب کتاب)

Satirical **dowry-awareness** experience: a Jetpack Compose Android client talks to a **FastAPI** backend that uses **Google Gemini 1.5 Flash** for image + text satire, plus **ReportLab** PDF exports on the server and **iText7** PDFs on-device.

- **Package:** `com.waseemsgith.jahaiz`
- **Default API URL:** `https://jahaiz-backend.onrender.com/` (override via Gradle — see below)
- **Branding:** every PDF export includes **“Built by Waseem Shareef K S | جہیز کا حساب کتاب”**.

## Repository layout

- `android/` — Android Studio / Gradle project (Compose, Hilt, Retrofit, Coil, Lottie, iText7).
- `backend/` — FastAPI service (`/api/image`, `/api/satire`, `/api/dowry`, `/api/pdf`) with `render.yaml` (root directory `backend`).
- `.github/workflows/deploy.yml` — optional Render deploy hook + debug APK build.

## Quick start — Android

1. Open the `android/` folder in Android Studio.
2. Create `android/local.properties` (or pass `-PAPI_BASE_URL=...` on the command line):

```properties
API_BASE_URL=https://your-service.onrender.com/
```

3. Sync Gradle and run on a device/emulator (min SDK **26**).

The app reads the base URL into `BuildConfig.BASE_URL` (no API keys are embedded).

## Quick start — Backend

```bash
cd backend
python -m venv .venv
source .venv/bin/activate  # Windows: .venv\Scripts\activate
pip install -r requirements.txt
cp .env.example .env  # set GEMINI_API_KEY
uvicorn app.main:app --reload --port 8000
```

Health check: `GET /health`

## Lottie assets

Placeholder JSON files live in `android/app/src/main/res/raw/`. Swap them with richer animations from [LottieFiles](https://lottiefiles.com) (search: “money rain”, “AI brain”, “scanning”, etc.) **without renaming** if you want instant wiring.

## Legal / licensing notes

- **Google Generative AI:** subject to Google AI Studio / Gemini terms.
- **iText 7 on Android:** AGPL/commercial licensing may apply for closed-source distribution — verify before publishing to Play.

## Deployment

See [DEPLOY.md](./DEPLOY.md) for Render + GitHub Actions + device install.

---

*Built with satire and care by Waseem Shareef K S — Kyunki Rishta Bhi Ek Business Hai 💸*
