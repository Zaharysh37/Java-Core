package com.innowise.minispring;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> scanPackage(String basePackage) throws Exception {
        List<Class<?>> classes = new LinkedList<>(); // Локальный список
        scanPackageRecursive(basePackage, classes);
        return classes;
    }

    private static void scanPackageRecursive(String basePackage, List<Class<?>> classes) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = basePackage.replace('.', '/');
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            throw new RuntimeException("Package not found: " + basePackage);
        }

        File directory = new File(resource.toURI());
        File[] files = directory.listFiles();

        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanPackageRecursive(basePackage + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                processClassFile(basePackage, file, classes);
            }
        }
    }

    private static void processClassFile(String basePackage, File file, List<Class<?>> classes) {
        try {
            String className = basePackage + "." + file.getName().substring(0, file.getName().length() - 6);
            Class<?> clazz = Class.forName(className);
            classes.add(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load class: " + e.getMessage());
        }
    }
}