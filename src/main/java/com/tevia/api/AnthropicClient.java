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
 * Client for the Anthropic Claude API.
 */
public class AnthropicClient implements LLMProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnthropicClient.class);
    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String ANTHROPIC_VERSION = "2023-06-01";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    private final String model;

    public AnthropicClient(String apiKey, String model, int timeoutSeconds) {
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
            throw new ApiException("Anthropic API key is not configured");
        }

        // Build request body
        JsonObject requestBody = buildRequestBody(request);

        // Create HTTP request
        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request httpRequest = new Request.Builder()
                .url(API_URL)
                .header("x-api-key", apiKey)
                .header("anthropic-version", ANTHROPIC_VERSION)
                .header("content-type", "application/json")
                .post(body)
                .build();

        LOGGER.debug("Sending request to Anthropic API: {}", requestBody);

        // Send request
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                LOGGER.error("Anthropic API error ({}): {}", response.code(), responseBody);
                throw new ApiException("Anthropic API request failed: " + response.code() + " - " + responseBody);
            }

            // Parse response
            return parseResponse(responseBody);

        } catch (IOException e) {
            LOGGER.error("Failed to communicate with Anthropic API", e);
            throw new ApiException("Network error communicating with Anthropic API", e);
        }
    }

    /**
     * Build the JSON request body for Anthropic API.
     */
    private JsonObject buildRequestBody(LLMRequest request) {
        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        body.addProperty("max_tokens", request.getMaxTokens());
        body.addProperty("temperature", request.getTemperature());

        // Add system message if present
        if (request.getSystem() != null && !request.getSystem().isEmpty()) {
            body.addProperty("system", request.getSystem());
        }

        // Add messages
        JsonArray messages = new JsonArray();
        for (LLMRequest.Message msg : request.getMessages()) {
            JsonObject message = new JsonObject();
            message.addProperty("role", msg.getRole());
            message.addProperty("content", msg.getContent());
            messages.add(message);
        }
        body.add("messages", messages);

        return body;
    }

    /**
     * Parse the JSON response from Anthropic API.
     */
    private LLMResponse parseResponse(String responseBody) {
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);

        LLMResponse response = new LLMResponse();
        response.setModel(json.get("model").getAsString());
        response.setStopReason(json.get("stop_reason").getAsString());

        // Extract content from first content block
        JsonArray content = json.getAsJsonArray("content");
        if (content.size() > 0) {
            JsonObject firstContent = content.get(0).getAsJsonObject();
            if (firstContent.has("text")) {
                response.setContent(firstContent.get("text").getAsString());
            }
        }

        // Extract token usage
        if (json.has("usage")) {
            JsonObject usage = json.getAsJsonObject("usage");
            response.setInputTokens(usage.get("input_tokens").getAsInt());
            response.setOutputTokens(usage.get("output_tokens").getAsInt());
        }

        LOGGER.debug("Received response from Anthropic API: {} tokens used",
                response.getInputTokens() + response.getOutputTokens());

        return response;
    }

    @Override
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @Override
    public String getProviderName() {
        return "anthropic";
    }

    @Override
    public String getModel() {
        return model;
    }
}
