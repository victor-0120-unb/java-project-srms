package com.chuka.srms.config;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class AppConfig {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "config/application.properties";
    
    static {
        try {
            Path configDir = Paths.get("config");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            File file = new File(CONFIG_FILE);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    properties.load(fis);
                }
            } else {
                setDefaultProperties();
                saveProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("persistence.mode", "file");
        properties.setProperty("database.url", "jdbc:mysql://localhost:3306/chuka_university_db?useSSL=false&serverTimezone=UTC");
        properties.setProperty("database.user", "srms_user");
        properties.setProperty("database.password", "Srms.149");
        properties.setProperty("data.directory", "./data");
    }
    
    private static void saveProperties() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "SRMS Configuration");
        }
    }
    
    public static String getPersistenceMode() {
        return properties.getProperty("persistence.mode", "file");
    }
    
    public static boolean isDatabaseMode() {
        return getPersistenceMode().equals("database");
    }
    
    public static boolean isFileMode() {
        return getPersistenceMode().equals("file");
    }
    
    public static void setPersistenceMode(String mode) {
        properties.setProperty("persistence.mode", mode);
        try {
            saveProperties();
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }
    
    public static String getDbUrl() {
        return properties.getProperty("database.url");
    }
    
    public static String getDbUser() {
        return properties.getProperty("database.user");
    }
    
    public static String getDbPassword() {
        return properties.getProperty("database.password");
    }
    
    public static String getDataDirectory() {
        return properties.getProperty("data.directory", "./data");
    }
    
    public static String getDataFilePath(String filename) {
        return getDataDirectory() + "/" + filename;
    }
    
    public static void ensureDataDirectory() throws IOException {
        Path dataDir = Paths.get(getDataDirectory());
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
    }
}
