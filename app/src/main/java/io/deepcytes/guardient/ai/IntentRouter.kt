package io.deepcytes.guardient.ai

enum class GuardientIntent {
    RISK_SCORE,
    MIC_APPS,
    CAMERA_APPS,
    LOCATION_APPS,
    SECURITY_TIPS,
    APP_SECURITY_DETAILS,
    GENERAL_SECURITY
}

object IntentRouter {
    fun resolveIntent(userQuery: String): GuardientIntent {
        val query = userQuery.lowercase()
        return when {
            query.contains("risk") || query.contains("score") -> GuardientIntent.RISK_SCORE
            query.contains("mic") || query.contains("microphone") || query.contains("audio") -> GuardientIntent.MIC_APPS
            query.contains("camera") || query.contains("cam") -> GuardientIntent.CAMERA_APPS
            query.contains("location") || query.contains("gps") || query.contains("track") -> GuardientIntent.LOCATION_APPS
            query.contains("tip") || query.contains("advice") || query.contains("recommend") -> GuardientIntent.SECURITY_TIPS
            query.contains("explain") || query.contains("detail") || query.contains("why is") -> GuardientIntent.APP_SECURITY_DETAILS
            else -> GuardientIntent.GENERAL_SECURITY
        }
    }
}
