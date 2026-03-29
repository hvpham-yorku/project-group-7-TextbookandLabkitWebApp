package com.example.demo.service;

import com.example.demo.domain.MaterialRequest;
import com.example.demo.domain.MaterialRequestInsight;
import com.example.demo.domain.MaterialRequestStatus;
import com.example.demo.repository.MaterialRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;

    public MaterialRequestService(MaterialRequestRepository materialRequestRepository) {
        this.materialRequestRepository = materialRequestRepository;
    }

    public MaterialRequest submitRequest(String requesterEmail, String courseCode,
                                         String materialTitle, String materialType, String note) {

        if (requesterEmail == null || requesterEmail.isBlank()) return null;
        if (courseCode == null || courseCode.isBlank()) return null;
        if (materialTitle == null || materialTitle.isBlank()) return null;

        MaterialRequest request = new MaterialRequest();
        request.setRequesterEmail(requesterEmail.trim());
        request.setCourseCode(normalizeCourseCode(courseCode));
        request.setMaterialTitle(materialTitle.trim());
        request.setMaterialType(materialType == null ? "" : materialType.trim());
        request.setNote(note == null ? "" : note.trim());
        request.setStatus(MaterialRequestStatus.OPEN);
        request.setCreatedAt(LocalDateTime.now());

        return materialRequestRepository.save(request);
    }

    public List<MaterialRequest> getRequestsForCourse(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) return List.of();
        return materialRequestRepository.findByCourseCode(normalizeCourseCode(courseCode));
    }

    public long getOpenRequestCountForCourse(String courseCode) {
        return getRequestsForCourse(courseCode).size();
    }

    public List<MaterialRequestInsight> getDemandInsightsForCourse(String courseCode) {
        List<MaterialRequest> requests = getRequestsForCourse(courseCode);
        if (requests.isEmpty()) return List.of();

        Map<String, Long> counts = requests.stream()
                .collect(Collectors.groupingBy(
                        request -> request.getMaterialType() == null || request.getMaterialType().isBlank()
                                ? "General request"
                                : request.getMaterialType().trim(),
                        Collectors.counting()
                ));

        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new MaterialRequestInsight(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<MaterialRequest> getRecentRequestsForCourse(String courseCode, int limit) {
        return getRequestsForCourse(courseCode).stream()
                .sorted(Comparator.comparing(MaterialRequest::getCreatedAt).reversed())
                .limit(limit)
                .toList();
    }

    private String normalizeCourseCode(String courseCode) {
        return courseCode.trim().toUpperCase(Locale.ROOT);
    }
}
