package com.innowise.skynet;

import com.innowise.skynet.entity.Faction;
import com.innowise.skynet.entity.Factory;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int DAYS = 100;
    private static final int NUM_FACTIONS = 2;

    public static void main(String[] args) throws InterruptedException {
        Factory factory = new Factory();

        CyclicBarrier barrier = new CyclicBarrier(NUM_FACTIONS + 1);

        Faction world = new Faction("World", factory, barrier);
        Faction wednesday = new Faction("Wednesday", factory, barrier);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_FACTIONS);
        executor.submit(world);
        executor.submit(wednesday);

        try {
            for (int day = 1; day <= DAYS; day++) {
                System.out.println("\n----- Day " + day + " -----");

                factory.produceParts();

                barrier.await();
                barrier.await();

                world.tryBuildRobots();
                wednesday.tryBuildRobots();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }

        System.out.println("\n----- RESULTS -----");
        System.out.println("World's army: " + world.getRobotsBuilt() + " robots.");
        System.out.println("Wednesday's army: " + wednesday.getRobotsBuilt() + " robots.");

        if (world.getRobotsBuilt() > wednesday.getRobotsBuilt()) {
            System.out.println("VICTORY: World has the strongest army!");
        } else if (wednesday.getRobotsBuilt() > world.getRobotsBuilt()) {
            System.out.println("VICTORY: Wednesday has the strongest army!");
        } else {
            System.out.println("RESULT: It's a draw!");
        }
    }
}