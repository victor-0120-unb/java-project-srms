package com.chuka.srms.service;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.TutorDAO;
import com.chuka.srms.dao.database.TutorDatabaseDAO;
import com.chuka.srms.dao.file.TutorFileDAO;
import com.chuka.srms.model.Tutor;
import java.util.List;

public class TutorService {
    private TutorDAO tutorDAO;
    
    public TutorService() {
        if (AppConfig.isDatabaseMode()) {
            tutorDAO = new TutorDatabaseDAO();
        } else {
            tutorDAO = new TutorFileDAO();
        }
    }
    
    public void addTutor(Tutor tutor) throws Exception {
        validateTutor(tutor);
        tutorDAO.addTutor(tutor);
    }
    
    public void updateTutor(Tutor tutor) throws Exception {
        validateTutor(tutor);
        tutorDAO.updateTutor(tutor);
    }
    
    public void deleteTutor(String tutorId) throws Exception {
        tutorDAO.deleteTutor(tutorId);
    }
    
    public Tutor getTutorById(String tutorId) throws Exception {
        return tutorDAO.getTutorById(tutorId)
            .orElseThrow(() -> new IllegalArgumentException("Tutor not found"));
    }
    
    public List<Tutor> getAllTutors() throws Exception {
        return tutorDAO.getAllTutors();
    }
    
    private void validateTutor(Tutor tutor) {
        if (tutor == null) throw new IllegalArgumentException("Tutor cannot be null");
        if (tutor.getTutorId() == null || tutor.getTutorId().trim().isEmpty())
            throw new IllegalArgumentException("Tutor ID required");
        if (tutor.getFirstName() == null || tutor.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("First name required");
        if (tutor.getLastName() == null || tutor.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("Last name required");
    }
}