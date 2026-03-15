package com.example.demo.service;

import com.example.demo.domain.CourseMaterial;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CourseMaterialService {

    private final Map<String, List<CourseMaterial>> materialsByCourse = new HashMap<>();

    public CourseMaterialService() {
        seed("EECS 2101",
                new CourseMaterial("EECS 2101",
                        "Fundamentals of Data Structures (course pack / textbook varies by term)",
                        "",
                        true,
                        null));

        seed("EECS 2311",
                new CourseMaterial("EECS 2311",
                        "Software Engineering: A Practitioner's Approach (or instructor-selected text)",
                        "",
                        false,
                        null));

        seed("EECS 2030",
                new CourseMaterial("EECS 2030",
                        "Core Java / OOP textbook (edition varies)",
                        "",
                        false,
                        null));

        seed("PHYS 1010",
                new CourseMaterial("PHYS 1010",
                        "PHYS 1010 Lab Manual (latest edition)",
                        "",
                        true,
                        new BigDecimal("35.00")));

        seed("MATH 1013",
                new CourseMaterial("MATH 1013",
                        "Course Notes / WebAssign access (varies)",
                        "",
                        true,
                        null));
    }

    private void seed(String courseCode, CourseMaterial... items) {
        String key = normalize(courseCode);
        materialsByCourse.putIfAbsent(key, new ArrayList<>());
        materialsByCourse.get(key).addAll(Arrays.asList(items));
    }

    public List<CourseMaterial> getMaterialsForCourse(String courseCode) {
        if (courseCode == null) return List.of();
        String key = normalize(courseCode);
        return materialsByCourse.getOrDefault(key, List.of());
    }

    public Set<String> getSeededCourseCodes() {
        return new TreeSet<>(materialsByCourse.keySet());
    }

    private String normalize(String s) {
        return s.trim().toUpperCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}