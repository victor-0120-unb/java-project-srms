package com.chuka.srms.dao;

import com.chuka.srms.model.ExaminationResult;
import java.util.List;
import java.util.Optional;

public interface ExaminationResultDAO {
    void addResult(ExaminationResult result) throws Exception;
    void updateResult(ExaminationResult result) throws Exception;
    void deleteResult(String resultId) throws Exception;
    Optional<ExaminationResult> getResultById(String resultId) throws Exception;
    List<ExaminationResult> getAllResults() throws Exception;
    List<ExaminationResult> getResultsByEnrollment(String enrollmentId) throws Exception;
    List<ExaminationResult> getResultsByStudent(String studentId) throws Exception;
    Optional<ExaminationResult> getResultByEnrollmentAndSemester(String enrollmentId, int semester, int year) throws Exception;
}
