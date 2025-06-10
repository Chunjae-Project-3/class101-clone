package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.repository.ClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;

    public List<ClassDTO> getClasses(String category) {
        return classRepository.getClasses(category);
    }
}
