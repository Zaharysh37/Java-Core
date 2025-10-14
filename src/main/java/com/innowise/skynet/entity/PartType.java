package com.innowise.skynet.entity;

public enum PartType {
    HEAD, TORSO, HAND, FOOT;

    public static PartType getRandomPart() {
        return values()[(int) (Math.random() * values().length)];
    }
}