package pro.sky.newmagicschool.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.newmagicschool.dto.FacultyDto;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.exception.FacultyNotFoundException;
import pro.sky.newmagicschool.mapper.FacultyMapper;
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
    private final FacultyMapper facultyMapper;
    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          StudentMapper studentMapper, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public FacultyDto findFacultyById(Long facultyId) {
        Faculty foundFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
        return facultyMapper.toDto(foundFaculty);

    }

    public FacultyDto updateFaculty(Faculty faculty) {
         Faculty changedFaculty = facultyRepository.findById(faculty.getId())
                .map(foundFaculty -> {
                    foundFaculty.setColor(faculty.getColor());
                    foundFaculty.setName(faculty.getName());
                    return facultyRepository.save(foundFaculty);
                })
                .orElseThrow(() -> new FacultyNotFoundException(faculty.getId()));
        return facultyMapper.toDto(changedFaculty);
    }

    public FacultyDto deleteFaculty(Long facultyId) {
        Faculty foundFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(facultyId));
        studentRepository.findAllByFaculty_Id(facultyId)
                        .forEach(Student::deleteFaculty);
        facultyRepository.delete(foundFaculty);
        return facultyMapper.toDto(foundFaculty);
    }

    public List<FacultyDto> findAll(@Nullable String color) {
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FacultyDto> getFacultyByColorOrName(String colorOrName) {
        return facultyRepository
                .findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(colorOrName, colorOrName)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDto> findStudents(Long id) {
        return studentRepository.findAllByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

}
