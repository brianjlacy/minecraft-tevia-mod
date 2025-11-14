package com.tevia.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a character profile that defines the AI's personality and behavior.
 */
public class CharacterProfile {

    private String name;
    private String personality;
    private String background;
    private List<String> goals;
    private String knowledgeLevel; // "novice", "intermediate", "expert"
    private List<String> behavioralTraits;
    private String specialInstructions;

    public CharacterProfile() {
        this.goals = new ArrayList<>();
        this.behavioralTraits = new ArrayList<>();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public String getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(String knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public List<String> getBehavioralTraits() {
        return behavioralTraits;
    }

    public void setBehavioralTraits(List<String> behavioralTraits) {
        this.behavioralTraits = behavioralTraits;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
