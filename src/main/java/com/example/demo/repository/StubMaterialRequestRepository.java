package com.example.demo.repository;

import com.example.demo.domain.MaterialRequest;
import com.example.demo.domain.MaterialRequestStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("stub")
public class StubMaterialRequestRepository implements MaterialRequestRepository {

    private final List<MaterialRequest> requests = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public StubMaterialRequestRepository() {
        seed("student1@my.yorku.ca", "EECS 2311", "Software Engineering Textbook", "Textbook",
                "Looking for a used copy under $60.", LocalDateTime.now().minusDays(2));
        seed("saif0@my.yorku.ca", "EECS 2311", "Project lab kit", "Lab Kit",
                "Need all core components included.", LocalDateTime.now().minusDays(1));
        seed("abc123@my.yorku.ca", "PHYS 1010", "Lab manual", "Lab Manual",
                "Latest section preferred.", LocalDateTime.now().minusHours(10));
        seed("student1@my.yorku.ca", "EECS 2030", "Object-oriented programming textbook", "Textbook",
                "Okay with highlights.", LocalDateTime.now().minusHours(4));
    }

    private void seed(String requesterEmail, String courseCode, String title, String materialType,
                      String note, LocalDateTime createdAt) {
        MaterialRequest request = new MaterialRequest();
        request.setId(idSeq.getAndIncrement());
        request.setRequesterEmail(requesterEmail);
        request.setCourseCode(courseCode);
        request.setMaterialTitle(title);
        request.setMaterialType(materialType);
        request.setNote(note);
        request.setStatus(MaterialRequestStatus.OPEN);
        request.setCreatedAt(createdAt);
        requests.add(request);
    }

    @Override
    public MaterialRequest save(MaterialRequest request) {
        request.setId(idSeq.getAndIncrement());
        requests.add(request);
        return request;
    }

    @Override
    public List<MaterialRequest> findAll() {
        List<MaterialRequest> copy = new ArrayList<>(requests);
        copy.sort(Comparator.comparing(MaterialRequest::getCreatedAt).reversed());
        return copy;
    }

    @Override
    public List<MaterialRequest> findByCourseCode(String courseCode) {
        List<MaterialRequest> result = new ArrayList<>();
        for (MaterialRequest request : requests) {
            if (request.getCourseCode() != null && request.getCourseCode().equalsIgnoreCase(courseCode)) {
                result.add(request);
            }
        }
        result.sort(Comparator.comparing(MaterialRequest::getCreatedAt).reversed());
        return result;
    }
}
