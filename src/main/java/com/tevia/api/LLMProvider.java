package com.tevia.api;

import com.tevia.api.models.LLMRequest;
import com.tevia.api.models.LLMResponse;

/**
 * Interface for LLM API providers.
 * Implementations handle communication with specific LLM services (Anthropic, Replicate, etc.).
 */
public interface LLMProvider {

    /**
     * Send a request to the LLM and get a response.
     *
     * @param request The LLM request containing messages and parameters
     * @return The LLM response containing the generated text
     * @throws Exception if the API call fails
     */
    LLMResponse sendRequest(LLMRequest request) throws Exception;

    /**
     * Check if the provider is properly configured (e.g., API key is set).
     *
     * @return true if the provider is ready to use
     */
    boolean isConfigured();

    /**
     * Get the name of this provider.
     *
     * @return Provider name (e.g., "anthropic", "replicate")
     */
    String getProviderName();

    /**
     * Get the current model being used.
     *
     * @return Model identifier
     */
    String getModel();
}
