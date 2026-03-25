package com.chuka.srms.dao.file;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.TutorDAO;
import com.chuka.srms.model.Tutor;
import com.chuka.srms.util.FileUtil;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TutorFileDAO implements TutorDAO {
    private static final String FILE_PATH;
    
    static {
        try {
            AppConfig.ensureDataDirectory();
            FILE_PATH = AppConfig.getDataFilePath("tutors.txt");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize tutor data file", e);
        }
    }
    
    @Override
    public void addTutor(Tutor tutor) throws IOException {
        if (getTutorById(tutor.getTutorId()).isPresent()) {
            throw new IllegalArgumentException("Tutor ID already exists: " + tutor.getTutorId());
        }
        FileUtil.writeLine(FILE_PATH, tutor.toString(), true);
    }
    
    @Override
    public void updateTutor(Tutor tutor) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            Tutor existing = Tutor.fromString(line);
            if (existing.getTutorId().equals(tutor.getTutorId())) {
                updatedLines.add(tutor.toString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Tutor not found: " + tutor.getTutorId());
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public void deleteTutor(String tutorId) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = lines.stream()
            .filter(line -> !Tutor.fromString(line).getTutorId().equals(tutorId))
            .collect(Collectors.toList());
        
        if (lines.size() == updatedLines.size()) {
            throw new IllegalArgumentException("Tutor not found: " + tutorId);
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public Optional<Tutor> getTutorById(String tutorId) throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Tutor::fromString)
            .filter(t -> t.getTutorId().equals(tutorId))
            .findFirst();
    }
    
    @Override
    public List<Tutor> getAllTutors() throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Tutor::fromString)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Tutor> getTutorsByDepartment(String department) throws IOException {
        return getAllTutors().stream()
            .filter(t -> t.getDepartment().equals(department))
            .collect(Collectors.toList());
    }
}
