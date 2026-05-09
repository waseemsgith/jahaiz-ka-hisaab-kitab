package com.waseemsgith.jahaiz.data.model

enum class OccupationType(
    val displayName: String,
    val category: String,
    val baseSatireMultiplier: Float,
) {
    IAS("IAS Officer", "Government Elite", 5.0f),
    IPS("IPS Officer", "Government Elite", 4.8f),
    IRS("IRS Officer", "Government Elite", 4.5f),
    UPSC_OTHER("UPSC Officer", "Government", 4.2f),
    RAILWAY_OFFICER("Railway Officer", "Government", 3.8f),
    PSU_EMPLOYEE("PSU Employee (ONGC/BHEL/etc)", "Government PSU", 3.5f),
    BANK_PO("Bank PO / Manager", "Government Finance", 3.3f),
    SSC_OFFICER("SSC CGL Officer", "Government", 3.0f),
    GOVERNMENT_TEACHER("Government Teacher", "Government Education", 2.8f),
    ARMY_OFFICER("Army Officer", "Defence", 3.6f),
    NAVY_OFFICER("Navy Officer", "Defence", 3.5f),
    AIR_FORCE("Air Force Officer", "Defence", 3.5f),
    POLICE_OFFICER("Police Officer (SI/Inspector)", "Government Police", 2.9f),
    SOFTWARE_ENGINEER("Software Engineer", "IT Private", 3.2f),
    AI_ENGINEER("AI/ML Engineer", "IT Premium", 3.8f),
    DEVOPS_ENGINEER("DevOps Engineer", "IT Private", 3.0f),
    DATA_SCIENTIST("Data Scientist", "IT Premium", 3.5f),
    CYBERSECURITY("Cybersecurity Engineer", "IT Private", 3.0f),
    FULLSTACK_DEV("Full Stack Developer", "IT Private", 3.1f),
    STARTUP_FOUNDER("Startup Founder", "Entrepreneur", 4.0f),
    IIT_GRADUATE("IIT Graduate", "Premier Education", 4.5f),
    NIT_GRADUATE("NIT Graduate", "Premier Education", 3.8f),
    IIIT_GRADUATE("IIIT Graduate", "Premier Education", 3.5f),
    IIM_MBA("IIM MBA", "Premier Management", 4.3f),
    MBBS_DOCTOR("MBBS Doctor", "Medical", 4.0f),
    SPECIALIST_DOCTOR("Specialist Doctor (MD/MS)", "Medical Elite", 4.8f),
    CA("Chartered Accountant", "Finance Professional", 3.8f),
    LAWYER("Lawyer / Advocate", "Legal", 3.2f),
    PROFESSOR("College Professor", "Education", 3.0f),
    DUBAI_EMPLOYEE("Dubai Employee", "Gulf Return", 4.5f),
    US_EMPLOYEE("US/Canada Employee", "Abroad Premium", 5.0f),
    UK_EMPLOYEE("UK Employee", "Abroad Premium", 4.8f),
    GULF_RETURN("Gulf Return (General)", "Gulf", 4.0f),
    BUSINESS_OWNER("Business Owner", "Business", 3.5f),
    INFLUENCER("Influencer / Content Creator", "Social Media", 2.5f),
    YOUTUBER("YouTuber", "Social Media", 2.5f),
    TRADER("Stock/Crypto Trader", "Finance", 2.8f),
    OTHER("Other / Private Job", "General", 2.0f),
    ;

    companion object {
        fun fromKey(key: String): OccupationType =
            entries.find { it.name == key } ?: OTHER
    }
}
