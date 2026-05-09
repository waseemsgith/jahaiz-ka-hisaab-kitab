from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", extra="ignore")

    GEMINI_API_KEY: str
    APP_NAME: str = "Jahaiz Ka Hisaab Kitab"
    DEBUG: bool = False


settings = Settings()
