package pro.sky.newmagicschool.service;

import org.springframework.stereotype.Service;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.exception.FacultyNotFoundException;
import pro.sky.newmagicschool.exception.IncorrectStudentAgeException;
import pro.sky.newmagicschool.exception.StudentNotFoundException;
import pro.sky.newmagicschool.mapper.StudentMapper;
import pro.sky.newmagicschool.repository.FacultyRepository;
import pro.sky.newmagicschool.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    public StudentService(StudentMapper studentMapper, StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
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

    public Student deleteStudent(Long studentId) {
        Student foundStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        studentRepository.delete(foundStudent);
        return foundStudent;
    }

    public List<Student> getStudentsByAge(int age) {
        if (age <= 0) {
            throw new IncorrectStudentAgeException(age);
        } else return studentRepository.findByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(int ageFrom, int ageTo) {
        if (ageFrom <= 0) {
            throw new IncorrectStudentAgeException(ageFrom);
        } else if (ageTo <= 0){
            throw new IncorrectStudentAgeException(ageTo);
        } else return studentRepository.findAllByAgeBetween(ageFrom, ageTo);
    }

    public Faculty findFaculty(Long id) {
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElseThrow(() -> new FacultyNotFoundException(id));
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
