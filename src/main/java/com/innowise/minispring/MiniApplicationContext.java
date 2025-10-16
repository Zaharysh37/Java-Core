package com.innowise.minispring;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniApplicationContext {
    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final Map<Class<?>, String> scopes = new HashMap<>();

    public MiniApplicationContext()
        throws Exception {
        List<Class<?>> candidates = ClassScanner.getClasses();

        for (Class<?> clazz : candidates) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Scope scopeAnn = clazz.getAnnotation(Scope.class);
                String scope = (scopeAnn == null) ? "singleton" : scopeAnn.value();
                scopes.put(clazz, scope);

                if (scope.equals("singleton")) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    singletons.put(clazz, instance);
                }
            }
        }

        for (Object instance : singletons.values()) {
            injectDependencies(instance);
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }
        }
    }

    public void injectDependencies(Object instance) throws Exception {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(instance, getBean(field.getType()));
            }
        }
    }

    public <T> T getBean(Class<T> type)
        throws Exception {
        String scope = scopes.getOrDefault(type, "singleton");
        if (scope.equals("singleton")) {
            return (T) singletons.get(type);
        } else {
            Object instance = type.getDeclaredConstructor().newInstance();
            injectDependencies(instance);
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }
            return (T) instance;
        }
    }
}
