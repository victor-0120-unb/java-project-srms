package com.chuka.srms.dao;

import com.chuka.srms.model.Tutor;
import java.util.List;
import java.util.Optional;

public interface TutorDAO {
    void addTutor(Tutor tutor) throws Exception;
    void updateTutor(Tutor tutor) throws Exception;
    void deleteTutor(String tutorId) throws Exception;
    Optional<Tutor> getTutorById(String tutorId) throws Exception;
    List<Tutor> getAllTutors() throws Exception;
    List<Tutor> getTutorsByDepartment(String department) throws Exception;
}

