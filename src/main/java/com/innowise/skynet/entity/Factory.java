package com.innowise.skynet.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class Factory {
    private final List<PartType> storage = new CopyOnWriteArrayList<>();

    public void produceParts() {
        int partsToProduce = 1 + (int) (Math.random() * 10);
        IntStream.range(0, partsToProduce).forEach(i -> storage.add(
            PartType.values()[(int)(Math.random()*PartType.values().length)]
        ));
        System.out.println("FACTORY: Produced " + partsToProduce + " parts. On stock: " + storage.size());
    }

    public PartType takePart() {
        if (!storage.isEmpty()) {
            return storage.removeFirst();
        }
        return null;
    }
}