package com.tevia.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Manages loading and saving character profiles.
 */
public class ProfileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileManager.class);

    private final Gson gson;
    private final Path profilesDir;

    public ProfileManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.profilesDir = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("tevia")
                .resolve("profiles");

        // Create profiles directory if it doesn't exist
        try {
            Files.createDirectories(profilesDir);
        } catch (IOException e) {
            LOGGER.error("Failed to create profiles directory", e);
        }
    }

    /**
     * Load a character profile from file.
     *
     * @param relativePath Path relative to config/tevia/profiles (e.g., "explorer.json")
     */
    public CharacterProfile loadProfile(String relativePath) throws IOException {
        // Remove "profiles/" prefix if present
        if (relativePath.startsWith("profiles/")) {
            relativePath = relativePath.substring("profiles/".length());
        }

        Path profilePath = profilesDir.resolve(relativePath);

        if (!Files.exists(profilePath)) {
            // Try to create default profile
            LOGGER.warn("Profile not found: {}, creating default", profilePath);
            CharacterProfile defaultProfile = createDefaultProfile();
            saveProfile(defaultProfile, relativePath);
            return defaultProfile;
        }

        String json = Files.readString(profilePath);
        CharacterProfile profile = gson.fromJson(json, CharacterProfile.class);

        LOGGER.info("Loaded profile from: {}", profilePath);
        return profile;
    }

    /**
     * Save a character profile to file.
     */
    public void saveProfile(CharacterProfile profile, String relativePath) throws IOException {
        Path profilePath = profilesDir.resolve(relativePath);

        Files.createDirectories(profilePath.getParent());

        String json = gson.toJson(profile);
        Files.writeString(profilePath, json);

        LOGGER.info("Saved profile to: {}", profilePath);
    }

    /**
     * Create a default character profile.
     */
    public CharacterProfile createDefaultProfile() {
        CharacterProfile profile = new CharacterProfile();

        profile.setName("Alex the Explorer");
        profile.setPersonality("Curious, adventurous, and friendly. Loves to explore new places and discover resources.");
        profile.setBackground("A wandering adventurer seeking to map out the world and collect rare materials.");
        profile.setGoals(Arrays.asList(
                "Explore the world and discover new biomes",
                "Collect valuable resources like diamonds and emeralds",
                "Build a safe shelter",
                "Be friendly with other players"
        ));
        profile.setKnowledgeLevel("intermediate");
        profile.setBehavioralTraits(Arrays.asList(
                "cautious", "resourceful", "social", "goal-oriented"
        ));
        profile.setSpecialInstructions(
                "Prioritize safety - avoid unnecessary combat unless defending yourself. " +
                "Always keep track of your health and hunger. " +
                "Engage with other players in a friendly manner."
        );

        return profile;
    }

    /**
     * Get the profiles directory path.
     */
    public Path getProfilesDir() {
        return profilesDir;
    }
}
