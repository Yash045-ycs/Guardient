package io.deepcytes.guardient.ai

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GuardientLlmManager(private val context: Context) {
    private var llmInference: LlmInference? = null

    /**
     * Initializes the MediaPipe Gemma model on a background thread.
     */
    suspend fun initModel(modelFileName: String = "guardient_gemma.task") = withContext(Dispatchers.IO) {
        val modelPath = "/data/local/tmp/llm/$modelFileName" // Or loaded via assets
        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(512)
            .setPreferredBackend(LlmInference.Backend.GPU)
            .build()

        llmInference = LlmInference.createFromOptions(context, options)
    }

    /**
     * Runs model inference asynchronously off the main thread.
     */
    suspend fun generateResponse(prompt: String): String = withContext(Dispatchers.IO) {
        return@withContext llmInference?.generateResponse(prompt) ?: ""
    }

    fun close() {
        llmInference?.close()
    }
}
