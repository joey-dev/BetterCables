package com.emorn.bettercables.core.common.dependencyInjection;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector
{
    private static final Map<Class<?>, Object> instances = new HashMap<>();
    private static final Map<Class<?>, Class<?>> interfaceMappings = ConfigLoader.loadImplementations();

    public static <T> T getInstance(Class<T> clazz)
    {
        try {
            if (instances.containsKey(clazz)) {
                return clazz.cast(instances.get(clazz));
            }

            // Resolve interface → implementation if needed
            if (clazz.isInterface() && interfaceMappings.containsKey(clazz)) {
                clazz = (Class<T>) interfaceMappings.get(clazz);
            }

            // Get constructor
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<?> constructor = constructors[0];

            // Resolve dependencies recursively
            Object[] params = resolveDependencies(constructor.getParameterTypes());
            T instance = clazz.cast(constructor.newInstance(params));

            instances.put(clazz, instance); // Cache instance
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }

    private static Object[] resolveDependencies(Class<?>[] paramTypes)
    {
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = getInstance(paramTypes[i]); // Recursively resolve dependencies
        }
        return params;
    }
}
