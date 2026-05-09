from __future__ import annotations

from typing import Optional

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", extra="ignore")

    # Optional at startup so uvicorn boots even before secrets are injected.
    # A missing key will cause a runtime error on the first Gemini API call,
    # not at server startup — allowing /health to pass on Render.
    GEMINI_API_KEY: Optional[str] = None
    APP_NAME: str = "Jahaiz Ka Hisaab Kitab"
    DEBUG: bool = False


settings = Settings()
