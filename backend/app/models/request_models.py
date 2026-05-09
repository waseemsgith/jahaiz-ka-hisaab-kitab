from pydantic import BaseModel, Field


class ImageAnalyzeJsonRequest(BaseModel):
    image_base64: str = Field(..., description="Base64-encoded image (jpeg/png)")
    mime_type: str = Field(default="image/jpeg", description="image/jpeg or image/png")


class OccupationSatireRequest(BaseModel):
    occupation: str
    salary: int = Field(ge=0, le=10_000_000)
    ego_level: int = Field(ge=1, le=10)
    abroad_status: str


class DowryCalculateRequest(BaseModel):
    occupation: str
    salary: int = Field(ge=0, le=10_000_000)
    abroad_status: str
    ego_level: int = Field(ge=1, le=10)
    family_expectation: int = Field(ge=1, le=10)
    luxury_level: int = Field(ge=1, le=10)
    gold_kg: float = Field(ge=0, le=1000)
    car: str
    wedding_level: str
    property_sqft: int | None = Field(default=None)


class PdfGenerateRequest(BaseModel):
    name: str
    photo_base64: str | None = None
    photo_mime: str | None = "image/jpeg"
    image_analysis: dict
    occupation_satire: dict
    dowry_invoice: dict
