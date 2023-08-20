package pro.sky.newmagicschool.service;


import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository,
                          StudentMapper studentMapper, FacultyMapper facultyMapper) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public Student createStudent(Student student) {
        logger.info("createStudent method was invoked");
        return studentRepository.save(student);
    }

    public StudentDto findStudentById(Long studentId) {
        logger.info("findStudentById method was invoked");
        Student foundStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("There is no student with id = " + studentId);
                    return new StudentNotFoundException(studentId);
                });
        return studentMapper.toDto(foundStudent);
    }

    public StudentDto updateStudent(StudentDto studentDto) {
        logger.info("updateStudent method was invoked");
        Student foundStudent = studentRepository.findById(studentDto.getId())
                .orElseThrow(() -> {
                    logger.error("There is no student with id = " + studentDto.getId());
                    return new StudentNotFoundException(studentDto.getId());
                });

        foundStudent.setName(studentDto.getName());
        foundStudent.setAge(studentDto.getAge());
        Faculty foundFaculty = facultyRepository.findById(studentDto.getFacultyId())
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id = " + studentDto.getFacultyId());
                    return new FacultyNotFoundException(studentDto.getFacultyId());
                });
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
        logger.info("deleteStudent method was invoked");
        Student foundStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("There is no student with id = " + studentId);
                    return new StudentNotFoundException(studentId);
                });
        studentRepository.delete(foundStudent);
        return studentMapper.toDto(foundStudent);
    }

    public List<StudentDto> getStudentsByAge(int age) {
        logger.info("getStudentsByAge method was invoked");
        if (age <= 0) {
            logger.error("Student couldn't have age = " + age);
            throw new IncorrectStudentAgeException(age);
        } else return studentRepository.findByAge(age)
                .stream()
                .map(studentMapper::toDto)
                .collect(toList());
    }

    public List<StudentDto> getStudentsByAgeBetween(int ageFrom, int ageTo) {
        logger.info("getStudentsByAgeBetween method was invoked");
        if (ageFrom <= 0) {
            logger.error("Student couldn't have age = " + ageFrom);
            throw new IncorrectStudentAgeException(ageFrom);
        } else if (ageTo <= 0){
            logger.error("Student couldn't have age = " + ageTo);
            throw new IncorrectStudentAgeException(ageTo);
        } else if (ageFrom > ageTo){
            logger.error("ageFrom couldn't be larger than ageTo");
            throw new IncorrectStudentAgeException(ageFrom, ageTo);
        } else return studentRepository.findAllByAgeBetween(ageFrom, ageTo)
                .stream()
                .map(studentMapper::toDto)
                .collect(toList());
    }

    public FacultyDto findFaculty(Long id) {
        logger.info("findFaculty method was invoked");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id = " + id);
                    return new FacultyNotFoundException(id);
                });
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

    public void taskThread() {
        List<Student> students = studentRepository.findAll();

        printStudent(students.get(0));
        printStudent(students.get(1));

        new Thread(() -> {
            printStudent(students.get(2));
            printStudent(students.get(3));
        }).start();

        new Thread(() -> {
            printStudent(students.get(4));
            printStudent(students.get(5));
        }).start();
    }



    public Integer countStudents() {
        return studentRepository.countSchoolStudents();
    }

    public Double getAverageStudentAge() {
        return studentRepository.countAverageAge();
    }

    public List<StudentDto> getLastStudents(int count) {
        return studentRepository.getLastStudents(Pageable.ofSize(count))
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    private void printStudent(Student student) {
        try {
            Thread.sleep(1000);
            logger.info(student.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void taskThreadSync() {
        List<Student> students = studentRepository.findAll();
        logger.info(students.toString());

        printStudentSync(students.get(0));
        printStudentSync(students.get(1));

        new Thread(() -> {
            printStudentSync(students.get(2));
            printStudentSync(students.get(3));
        }).start();

        new Thread(() -> {
            printStudentSync(students.get(4));
            printStudentSync(students.get(5));
        }).start();
    }

    private synchronized void printStudentSync(Student student) {
        printStudent(student);

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
