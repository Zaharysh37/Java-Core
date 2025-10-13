package com.innowise.skynet.entities;

import com.innowise.skynet.enums.PartType;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public class Faction implements Runnable {
    private final String name;
    private final Factory factory;
    private final CyclicBarrier barrier;
    private int robotsBuilt = 0;
    private final Map<PartType, Integer> collectedParts = new ConcurrentHashMap<>();

    private final int ATTACK_CARRY_CAPACITY = 5;

    public Faction(String name, Factory factory, CyclicBarrier barrier) {
        this.name = name;
        this.factory = factory;
        this.barrier = barrier;
        for (PartType part : PartType.values()) {
            collectedParts.put(part, 0);
        }
    }

    private void stealParts() {
        System.out.println(name + ": Starting night attack...");
        for (int i = 0; i < ATTACK_CARRY_CAPACITY; i++) {
            PartType part = factory.takePart();
            if (part != null) {
                collectedParts.merge(part, 1, Integer::sum);
                System.out.println(name + ": Stole a " + part);
            } else {
                System.out.println(name + ": Factory is empty, ending attack.");
                break;
            }
        }
    }

    public void tryBuildRobots() {
        while (true) {
            int heads = collectedParts.getOrDefault(PartType.HEAD, 0);
            int torsos = collectedParts.getOrDefault(PartType.TORSO, 0);
            int hands = collectedParts.getOrDefault(PartType.HAND, 0);
            int feet = collectedParts.getOrDefault(PartType.FOOT, 0);

            // Проверяем, хватает ли деталей для одного робота
            if (heads >= 1 && torsos >= 1 && hands >= 2 && feet >= 2) {
                // Списываем детали
                collectedParts.put(PartType.HEAD, heads - 1);
                collectedParts.put(PartType.TORSO, torsos - 1);
                collectedParts.put(PartType.HAND, hands - 2);
                collectedParts.put(PartType.FOOT, feet - 2);
                robotsBuilt++;
                System.out.println("SUCCESS: " + name + " built a robot! Total army: " + robotsBuilt);
            } else {
                break;
            }
        }
    }

    public int getRobotsBuilt() {
        return robotsBuilt;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                barrier.await();

                stealParts();

                barrier.await();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " was interrupted and is stopping.");
        }
    }
}