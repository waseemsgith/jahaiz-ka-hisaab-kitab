# Deployment Steps

## Step 1: Get Gemini API Key (FREE)

1. Open [Google AI Studio](https://aistudio.google.com).
2. Use **Get API key** → create a key for a Google Cloud project.
3. Copy the key into `backend/.env` as `GEMINI_API_KEY=...` (never commit this file).

## Step 2: Deploy Backend to Render (FREE)

1. Push this repository to GitHub under your account.
2. In [Render](https://render.com): **New → Web Service** → connect the repo.
3. Set **Root Directory** to `backend` (or use the included `render.yaml` blueprint).
4. **Build command:** `pip install -r requirements.txt`
5. **Start command:** `uvicorn app.main:app --host 0.0.0.0 --port $PORT`
6. Add environment variable **`GEMINI_API_KEY`** in the Render dashboard.
7. After deploy, copy your public URL, e.g. `https://jahaiz-backend.onrender.com/`.

### Optional: deploy hook (GitHub Actions)

1. In Render service settings, create a **Deploy Hook** URL.
2. In GitHub repository secrets, add `RENDER_DEPLOY_HOOK_URL` with that URL.
3. Pushes to `main` that touch `backend/**` will call the hook (see `.github/workflows/deploy.yml`).

## Step 3: Connect Android App

1. Edit `android/local.properties`:

```properties
API_BASE_URL=https://your-service.onrender.com/
```

2. Android Studio → **Sync Gradle** (this value is injected into `BuildConfig.BASE_URL`).

For CI builds, provide the same URL as GitHub Actions secret `RENDER_BACKEND_URL`.

## Step 4: Build APK

- **Android Studio:** Build → Build Bundle(s) / APK(s) → **Debug APK** (`app-debug.apk`).
- **CLI:** from `android/`, generate a Gradle wrapper once (`gradle wrapper --gradle-version 8.7`) then:

```bash
./gradlew assembleDebug
```

Transfer the APK to a device and install (enable **Install unknown apps** for your file/browser source).

## Step 5: Smoke test checklist

1. Splash + loading flow reaches **Home**.
2. Form → attach photo (optional) → **Hisaab Lagao**.
3. Processing overlay reaches **Result** without crash.
4. **Share** text works.
5. **PDF export** opens a PDF with footer **Built by Waseem Shareef K S | جہیز کا حساب کتاب**.

---

If Gemini quota or networking fails, the backend returns structured fallbacks so the demo UI still works while you fix credentials or connectivity.
