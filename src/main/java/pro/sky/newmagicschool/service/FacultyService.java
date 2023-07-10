package pro.sky.newmagicschool.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.exception.FacultyNotFoundException;
import pro.sky.newmagicschool.mapper.StudentMapper;
import pro.sky.newmagicschool.repository.FacultyRepository;
import pro.sky.newmagicschool.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFacultyById(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.findById(faculty.getId())
                .map(foundFaculty -> {
                    foundFaculty.setColor(faculty.getColor());
                    foundFaculty.setName(faculty.getName());
                    return facultyRepository.save(foundFaculty);
                })
                .orElseThrow(() -> new FacultyNotFoundException(faculty.getId()));
    }

    public Faculty deleteFaculty(Long facultyId) {
        Faculty foundFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
        facultyRepository.delete(foundFaculty);
        return foundFaculty;
    }

    public List<Faculty> findAll(@Nullable String color) {
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .collect(Collectors.toList());
    }

    public List<Faculty> getFacultyByColorOrName(String colorOrName) {
        return facultyRepository.findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(colorOrName, colorOrName);
    }

    public List<StudentDto> findStudents(Long id) {
        return studentRepository.findAllByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

}
