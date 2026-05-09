# Deployment Steps

## Step 1: Get Gemini API Key (FREE)

1. Open [Google AI Studio](https://aistudio.google.com).
2. Use **Get API key** → create a key for a Google Cloud project.
3. Copy the key into `backend/.env` as `GEMINI_API_KEY=...` (never commit this file).

## Step 2: Deploy Backend to Render (FREE)

The backend is deployed with **Docker** so production always uses **Python 3.11** from `backend/Dockerfile` (avoids native Render default **3.14** and broken **Pillow** source builds).

### Option A — Blueprint (recommended)

1. Push this repository to GitHub.
2. In Render: **New → Blueprint** → select the repo → Render loads **`render.yaml`** at the **repository root**.
3. Set secret **`GEMINI_API_KEY`** when prompted (or add it under the service **Environment** after sync).
4. Deploy. The image builds from **`./backend/Dockerfile`** with context **`./backend`**.

### Option B — Manual Docker web service

1. **New → Web Service** → connect the repo.
2. Set **Language / Runtime** to **Docker** (not Python).
3. **Dockerfile path:** `backend/Dockerfile`  
   **Docker build context:** `backend`
4. Add **`GEMINI_API_KEY`** in **Environment**.
5. Health check path: **`/health`** (optional but matches the app).

### Migrating an existing **native Python** service

Change the service to **Docker** in the dashboard (or recreate the service) so it no longer uses the native Python runtime. The old **build/start** commands are replaced by the Dockerfile **`CMD`**.

After deploy, copy your public URL, e.g. `https://jahaiz-backend.onrender.com/`.

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
