package pro.sky.newmagicschool.service;

import org.springframework.stereotype.Service;
import pro.sky.newmagicschool.dto.FacultyDto;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.exception.FacultyNotFoundException;
import pro.sky.newmagicschool.exception.IncorrectStudentAgeException;
import pro.sky.newmagicschool.exception.StudentNotFoundException;
import pro.sky.newmagicschool.mapper.FacultyMapper;
import pro.sky.newmagicschool.mapper.StudentMapper;
import pro.sky.newmagicschool.repository.FacultyRepository;
import pro.sky.newmagicschool.repository.StudentRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository,
                          StudentMapper studentMapper, FacultyMapper facultyMapper) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public StudentDto findStudentById(Long studentId) {
        Student foundStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        return studentMapper.toDto(foundStudent);
    }

    public StudentDto updateStudent(StudentDto studentDto) {
        Student foundStudent = studentRepository.findById(studentDto.getId())
                .orElseThrow(() -> new StudentNotFoundException(studentDto.getId()));

        foundStudent.setName(studentDto.getName());
        foundStudent.setAge(studentDto.getAge());
        Faculty foundFaculty = facultyRepository.findById(studentDto.getFacultyId())
                .orElseThrow(() -> new FacultyNotFoundException(studentDto.getFacultyId()));
        foundStudent.setFaculty(foundFaculty);

        /*
        Optional.ofNullable(studentDto.getFacultyId())
                            .ifPresent(facultyId ->
                                    foundStudent.setFaculty(
                                            facultyRepository.findById(facultyId)
                                        .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                                    )
                            );

         */
        return studentMapper.toDto(studentRepository.save(foundStudent));
    }

    public StudentDto deleteStudent(Long studentId) {
        Student foundStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        studentRepository.delete(foundStudent);
        return studentMapper.toDto(foundStudent);
    }

    public List<StudentDto> getStudentsByAge(int age) {
        if (age <= 0) {
            throw new IncorrectStudentAgeException(age);
        } else return studentRepository.findByAge(age)
                .stream()
                .map(studentMapper::toDto)
                .collect(toList());
    }

    public List<StudentDto> getStudentsByAgeBetween(int ageFrom, int ageTo) {
        if (ageFrom <= 0) {
            throw new IncorrectStudentAgeException(ageFrom);
        } else if (ageTo <= 0){
            throw new IncorrectStudentAgeException(ageTo);
        } else if (ageFrom > ageTo){
            throw new IncorrectStudentAgeException(ageFrom, ageTo);
        } else return studentRepository.findAllByAgeBetween(ageFrom, ageTo)
                .stream()
                .map(studentMapper::toDto)
                .collect(toList());
    }

    public FacultyDto findFaculty(Long id) {
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public List<String> getNamesStartWithA() {
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(toList());
    }

    public double getAvgAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .getAsDouble();
    }


    /*
      public StudentDtoOut uploadAvatar(long id, MultipartFile multipartFile) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));
    Avatar avatar = avatarService.create(student, multipartFile);
    StudentDtoOut studentDtoOut = studentMapper.toDto(student);
    studentDtoOut.setAvatarUrl("http://localhost:8080/avatars/" + avatar.getId() + "/from-db");
    return studentDtoOut;
  }
     */
}
