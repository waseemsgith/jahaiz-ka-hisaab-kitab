from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.config import settings
from app.routers import dowry, image, pdf, satire

app = FastAPI(
    title="Jahaiz Ka Hisaab Kitab API",
    description="Satirical AI backend for dowry awareness app",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(satire.router, prefix="/api/satire", tags=["Satire"])
app.include_router(image.router, prefix="/api/image", tags=["Image Analysis"])
app.include_router(dowry.router, prefix="/api/dowry", tags=["Dowry Calculator"])
app.include_router(pdf.router, prefix="/api/pdf", tags=["PDF"])


@app.get("/")
async def root():
    return {
        "app": settings.APP_NAME,
        "status": "online",
        "version": "1.0.0",
        "message": "Rishte Ka Rate Card Tayyar Hai! 📋",
    }


@app.get("/health")
async def health():
    return {"status": "healthy"}
