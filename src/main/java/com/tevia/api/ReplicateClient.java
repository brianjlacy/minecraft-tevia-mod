package com.tevia.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tevia.api.exceptions.ApiException;
import com.tevia.api.models.LLMRequest;
import com.tevia.api.models.LLMResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Client for the Replicate API.
 * Supports various open-source models like Llama, Mistral, etc.
 */
public class ReplicateClient implements LLMProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicateClient.class);
    private static final String API_URL = "https://api.replicate.com/v1/predictions";
    private static final int MAX_POLL_ATTEMPTS = 60; // Max 60 attempts
    private static final int POLL_INTERVAL_MS = 1000; // Poll every second

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    private final String model;

    public ReplicateClient(String apiKey, String model, int timeoutSeconds) {
        this.apiKey = apiKey;
        this.model = model;
        this.gson = new Gson();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public LLMResponse sendRequest(LLMRequest request) throws Exception {
        if (!isConfigured()) {
            throw new ApiException("Replicate API key is not configured");
        }

        // Build request body
        JsonObject requestBody = buildRequestBody(request);

        // Create prediction
        String predictionId = createPrediction(requestBody);

        // Poll for completion
        return pollForResult(predictionId);
    }

    /**
     * Create a prediction on Replicate.
     */
    private String createPrediction(JsonObject requestBody) throws ApiException, IOException {
        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request httpRequest = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Token " + apiKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        LOGGER.debug("Creating Replicate prediction: {}", requestBody);

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                LOGGER.error("Replicate API error ({}): {}", response.code(), responseBody);
                throw new ApiException("Replicate API request failed: " + response.code() + " - " + responseBody);
            }

            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            String predictionId = json.get("id").getAsString();
            LOGGER.debug("Prediction created: {}", predictionId);

            return predictionId;
        }
    }

    /**
     * Poll for the prediction result.
     */
    private LLMResponse pollForResult(String predictionId) throws Exception {
        String pollUrl = API_URL + "/" + predictionId;

        for (int attempt = 0; attempt < MAX_POLL_ATTEMPTS; attempt++) {
            Request httpRequest = new Request.Builder()
                    .url(pollUrl)
                    .header("Authorization", "Token " + apiKey)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to poll prediction: " + response.code());
                }

                JsonObject json = gson.fromJson(responseBody, JsonObject.class);
                String status = json.get("status").getAsString();

                LOGGER.debug("Prediction status: {}", status);

                if ("succeeded".equals(status)) {
                    return parseSuccessResponse(json);
                } else if ("failed".equals(status) || "canceled".equals(status)) {
                    String error = json.has("error") ? json.get("error").getAsString() : "Unknown error";
                    throw new ApiException("Prediction failed: " + error);
                }

                // Still processing, wait and retry
                Thread.sleep(POLL_INTERVAL_MS);
            }
        }

        throw new ApiException("Prediction timed out after " + MAX_POLL_ATTEMPTS + " attempts");
    }

    /**
     * Build the JSON request body for Replicate API.
     */
    private JsonObject buildRequestBody(LLMRequest request) {
        JsonObject body = new JsonObject();
        body.addProperty("version", model);

        // Build input object
        JsonObject input = new JsonObject();

        // Combine system and messages into a single prompt
        StringBuilder prompt = new StringBuilder();
        if (request.getSystem() != null && !request.getSystem().isEmpty()) {
            prompt.append(request.getSystem()).append("\n\n");
        }

        for (LLMRequest.Message msg : request.getMessages()) {
            String role = msg.getRole().equals("user") ? "User" : "Assistant";
            prompt.append(role).append(": ").append(msg.getContent()).append("\n\n");
        }
        prompt.append("Assistant: ");

        input.addProperty("prompt", prompt.toString());
        input.addProperty("max_length", request.getMaxTokens());
        input.addProperty("temperature", request.getTemperature());

        body.add("input", input);

        return body;
    }

    /**
     * Parse the success response from Replicate API.
     */
    private LLMResponse parseSuccessResponse(JsonObject json) {
        LLMResponse response = new LLMResponse();
        response.setModel(model);
        response.setStopReason("end_turn");

        // Extract output (usually an array of strings)
        if (json.has("output")) {
            JsonArray output = json.getAsJsonArray("output");
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < output.size(); i++) {
                content.append(output.get(i).getAsString());
            }
            response.setContent(content.toString().trim());
        }

        LOGGER.debug("Received response from Replicate API");

        return response;
    }

    @Override
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @Override
    public String getProviderName() {
        return "replicate";
    }

    @Override
    public String getModel() {
        return model;
    }
}
