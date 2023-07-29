package pro.sky.newmagicschool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);
    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          StudentMapper studentMapper, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("createFaculty method was invoked");
        return facultyRepository.save(faculty);
    }

    public FacultyDto findFacultyById(Long facultyId) {
        logger.info("findFacultyById method was invoked");
        Faculty foundFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id = " + facultyId);
                    return new FacultyNotFoundException(facultyId);
                });
        return facultyMapper.toDto(foundFaculty);

    }

    public FacultyDto updateFaculty(Faculty faculty) {
        logger.info("updateFaculty method was invoked");
         Faculty changedFaculty = facultyRepository.findById(faculty.getId())
                .map(foundFaculty -> {
                    foundFaculty.setColor(faculty.getColor());
                    foundFaculty.setName(faculty.getName());
                    return facultyRepository.save(foundFaculty);
                })
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id = " + faculty.getId());
                    return new FacultyNotFoundException(faculty.getId());
                });
        return facultyMapper.toDto(changedFaculty);
    }

    public FacultyDto deleteFaculty(Long facultyId) {
        logger.info("deleteFaculty method was invoked");
        Faculty foundFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id = " + facultyId);
                    return new FacultyNotFoundException(facultyId);
                });
        studentRepository.findAllByFaculty_Id(facultyId)
                        .forEach(Student::deleteFaculty);
        facultyRepository.delete(foundFaculty);
        return facultyMapper.toDto(foundFaculty);
    }

    public List<FacultyDto> findAll(@Nullable String color) {
        logger.info("findAll method was invoked");
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FacultyDto> getFacultyByColorOrName(String colorOrName) {
        logger.info("getFacultyByColorOrName method was invoked");
        return facultyRepository
                .findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(colorOrName, colorOrName)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDto> findStudents(Long id) {
        logger.info("findStudents method was invoked");
        return studentRepository.findAllByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public Integer sum() {
        long start = System.currentTimeMillis();
        int res = Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        long finish = System.currentTimeMillis();
        long dif = finish - start;
        System.out.println("simple " + dif);
        return res;

    }

    public Integer sumImpr() {
        long start = System.currentTimeMillis();
        int res = Stream.iterate(1, a -> a +1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        long finish = System.currentTimeMillis();
        long dif = finish - start;
        System.out.println("impr " + dif);
        return res;

    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .get();
    }
}
