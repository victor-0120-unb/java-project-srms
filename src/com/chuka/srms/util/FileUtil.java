package com.chuka.srms.util;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileUtil {
    
    public static List<String> readAllLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
            return List.of();
        }
        return Files.readAllLines(path);
    }
    
    public static void writeLine(String filePath, String line, boolean append) throws IOException {
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, 
                append ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)) {
            writer.write(line);
            writer.newLine();
        }
    }
    
    public static void rewriteFile(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public static void clearFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}