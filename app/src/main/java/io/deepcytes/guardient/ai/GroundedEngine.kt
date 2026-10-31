package io.deepcytes.guardient.ai

import org.json.JSONObject

object GroundedEngine {

    fun buildSystemPrompt(userQuery: String, deviceFactsJson: String): String {
        return """
            System: You are Guardient, an on-device security assistant.
            Analyze the provided real device facts and user query. Output strictly valid JSON.
            Never invent package names, scores, or permissions.

            Real Device Facts:
            $deviceFactsJson

            User Query: $userQuery

            Response Format (Schema Version 1):
            {
              "schemaVersion": 1,
              "intent": "INTENT_NAME",
              "message": "Explanation text",
              "apps": [],
              "suggestedActions": []
            }
            JSON Output:
        """.trimIndent()
    }

    fun validateOrFallback(
        rawOutput: String, 
        fallbackIntent: GuardientIntent, 
        fallbackMessage: String
    ): String {
        return try {
            val jsonStart = rawOutput.indexOf("{")
            val jsonEnd = rawOutput.lastIndexOf("}")
            if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                val extractedJson = rawOutput.substring(jsonStart, jsonEnd + 1)
                val jsonObject = JSONObject(extractedJson)
                if (jsonObject.has("schemaVersion") && jsonObject.has("intent")) {
                    return jsonObject.toString()
                }
            }
            buildFallbackJson(fallbackIntent, fallbackMessage)
        } catch (e: Exception) {
            buildFallbackJson(fallbackIntent, fallbackMessage)
        }
    }

    private fun buildFallbackJson(intent: GuardientIntent, message: String): String {
        val root = JSONObject()
        root.put("schemaVersion", 1)
        root.put("intent", intent.name)
        root.put("message", message)
        root.put("apps", org.json.JSONArray())
        root.put("suggestedActions", org.json.JSONArray().put("Review permissions in settings"))
        return root.toString()
    }
}
