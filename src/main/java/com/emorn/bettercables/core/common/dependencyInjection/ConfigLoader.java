package com.emorn.bettercables.core.common.dependencyInjection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ConfigLoader
{
    public static String getVersion()
    {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            return prop.getProperty("version", "1.12.2"); // Default version
        } catch (IOException e) {
            e.printStackTrace();
            return "1.12.2"; // Default fallback
        }
    }

    public static Map<Class<?>, Class<?>> loadImplementations()
    {
        String version = getVersion();
        String interfacePackage = "contract"; // Interfaces live here (including subfolders)
        String implementationPackage = "api.v" + version.replace(".", "_"); // Example: api.v1_12_2

        Map<Class<?>, Class<?>> interfaceToImpl = new HashMap<>();

        // Find all interfaces (including nested subfolders)
        for (Class<?> iface : findClasses(interfacePackage)) {
            if (iface.isInterface()) {
                // Find its implementation in the correct versioned package
                Class<?> implementation = findImplementation(implementationPackage, iface);
                if (implementation != null) {
                    interfaceToImpl.put(iface, implementation);
                }
            }
        }

        return interfaceToImpl;
    }

    private static Class<?> findImplementation(
        String packageName,
        Class<?> interfaceClass
    )
    {
        for (Class<?> clazz : findClasses(packageName)) {
            if (interfaceClass.isAssignableFrom(clazz) && !clazz.isInterface()) {
                return clazz;
            }
        }
        return null;
    }

    private static List<Class<?>> findClasses(String packageName)
    {
        List<Class<?>> classList = new ArrayList<>();
        try {
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);

            if (resource == null) {
                return classList;
            }

            File directory = new File(resource.getFile());
            if (!directory.exists() || !directory.isDirectory()) {
                return classList;
            }

            scanDirectory(directory, packageName, classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static void scanDirectory(
        File directory,
        String packageName,
        List<Class<?>> classList
    )
    {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively scan subdirectories
                scanDirectory(file, packageName + "." + file.getName(), classList);
            } else if (file.getName().endsWith(".java")) {
                try {
                    String className = packageName + "." + file.getName().replace(".java", "");
                    Class<?> clazz = Class.forName(className);
                    classList.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

