package com.example.demo.repository;

import com.example.demo.domain.MaterialRequest;

import java.util.List;

public interface MaterialRequestRepository {
    MaterialRequest save(MaterialRequest request);
    List<MaterialRequest> findAll();
    List<MaterialRequest> findByCourseCode(String courseCode);
}
