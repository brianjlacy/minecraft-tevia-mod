package com.tevia.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProfileManager.
 */
class ProfileManagerTest {

    private ProfileManager profileManager;

    @BeforeEach
    void setUp() {
        profileManager = new ProfileManager();
    }

    @Test
    void testCreateDefaultProfile() {
        CharacterProfile profile = profileManager.createDefaultProfile();

        assertNotNull(profile);
        assertNotNull(profile.getName());
        assertNotNull(profile.getPersonality());
        assertFalse(profile.getGoals().isEmpty());
        assertFalse(profile.getBehavioralTraits().isEmpty());
    }

    @Test
    void testProfileFields() {
        CharacterProfile profile = new CharacterProfile();

        profile.setName("Test Character");
        profile.setPersonality("Friendly");
        profile.setKnowledgeLevel("expert");

        assertEquals("Test Character", profile.getName());
        assertEquals("Friendly", profile.getPersonality());
        assertEquals("expert", profile.getKnowledgeLevel());
    }
}
