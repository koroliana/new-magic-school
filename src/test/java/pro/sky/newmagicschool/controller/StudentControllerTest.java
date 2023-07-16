package pro.sky.newmagicschool.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.newmagicschool.dto.FacultyDto;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.repository.FacultyRepository;
import pro.sky.newmagicschool.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;
    String name1 = "Viktor Krum";
    int age1 = 18;

    String name2 = "Виктор Крам";
    int age2 = 19;
    Student student1 = new Student();

    long incorrectId = 111L;


    @BeforeEach
    public void beforeEach() {
        student1.setName(name1);
        student1.setAge(age1);

    }


    @AfterEach
    public void clean() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }



    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void createStudentTest() {

        ResponseEntity<Student> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student1,
                Student.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Student student = responseEntity.getBody();

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotEqualTo(0L);
        assertThat(student.getAge()).isEqualTo(student1.getAge());
        assertThat(student.getName()).isEqualTo(student1.getName());
    }

    @Test
    public void editStudentTestNotFound() {

        studentRepository.save(student1);

        StudentDto studentDto = new StudentDto();
        studentDto.setId(incorrectId);
        studentDto.setName(name2);
        studentDto.setAge(age2);



        ResponseEntity<StudentDto> updateStudentResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        );

        assertThat(updateStudentResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


    @Test
    public void editStudentTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Test");
        faculty.setColor("Test");

        Faculty createdFaculty = facultyRepository.save(faculty);

        Student createdStudent = studentRepository.save(student1);

        StudentDto studentDto = new StudentDto();
        studentDto.setId(createdStudent.getId());
        studentDto.setName(name2);
        studentDto.setAge(age2);
        studentDto.setFacultyId(createdFaculty.getId());

        ResponseEntity<StudentDto> updateStudentResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        );
        assertThat(updateStudentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDto updatedStudentDto = updateStudentResponse.getBody();

        assertThat(updatedStudentDto).isNotNull();
        assertThat(updatedStudentDto.getId()).isEqualTo(studentDto.getId());
        assertThat(updatedStudentDto.getAge()).isEqualTo(studentDto.getAge());
        assertThat(updatedStudentDto.getName()).isEqualTo(studentDto.getName());
    }

    @Test
    public void deleteStudentTestNotFound() {
        ResponseEntity<String> deleteStudentResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student" + incorrectId,
                HttpMethod.DELETE,
                //new HttpEntity<>(""),
                null,
                String.class
        );

        assertThat(deleteStudentResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void deleteStudentTest() {
        Student createdStudent = studentRepository.save(student1);
        Long correctID = createdStudent.getId();

        StudentDto studentDto = new StudentDto();
        studentDto.setId(correctID);
        studentDto.setName(student1.getName());
        studentDto.setAge(student1.getAge());


        ResponseEntity<StudentDto> deleteStudentResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + correctID,
                HttpMethod.DELETE,
              //  new HttpEntity<>(""),
                null,
                StudentDto.class
        );

        assertThat(deleteStudentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDto deletedStudentDto = deleteStudentResponse.getBody();

        assertThat(deletedStudentDto).isNotNull();
        assertThat(deletedStudentDto.getId()).isEqualTo(studentDto.getId());
        assertThat(deletedStudentDto.getAge()).isEqualTo(studentDto.getAge());
        assertThat(deletedStudentDto.getName()).isEqualTo(studentDto.getName());

    }

    @Test
    public void getStudentInfoTestNotFound() {
        ResponseEntity<StudentDto> getStudentInfoResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + incorrectId,
                HttpMethod.GET,
                null,
                StudentDto.class
        );

        assertThat(getStudentInfoResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getStudentInfoTest() {
        Student createdStudent = studentRepository.save(student1);
        Long correctID = createdStudent.getId();

        StudentDto studentDto = new StudentDto();
        studentDto.setId(correctID);
        studentDto.setName(student1.getName());
        studentDto.setAge(student1.getAge());


        ResponseEntity<StudentDto> getStudentInfoResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + correctID,
                HttpMethod.GET,
                null,
                StudentDto.class
        );

        assertThat(getStudentInfoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDto foundStudentDto = getStudentInfoResponse.getBody();

        assertThat(foundStudentDto).isNotNull();
        assertThat(foundStudentDto.getId()).isEqualTo(studentDto.getId());
        assertThat(foundStudentDto.getAge()).isEqualTo(studentDto.getAge());
        assertThat(foundStudentDto.getName()).isEqualTo(studentDto.getName());

    }


    @Test
    public void getStudentsByAgeTest() {
        Student student2 = new Student();
        student2.setName(name2);
        student2.setAge(age1);

        Student createdStudent1 = studentRepository.save(student1);
        Student createdStudent2 = studentRepository.save(student2);

        StudentDto studentDto1 = new StudentDto();
        studentDto1.setId(createdStudent1.getId());
        studentDto1.setName(createdStudent1.getName());
        studentDto1.setAge(createdStudent1.getAge());

        StudentDto studentDto2 = new StudentDto();
        studentDto2.setId(createdStudent2.getId());
        studentDto2.setName(createdStudent2.getName());
        studentDto2.setAge(createdStudent2.getAge());

        List<StudentDto> studentDtoList = new ArrayList<>();
        studentDtoList.add(studentDto1);
        studentDtoList.add(studentDto2);


        ResponseEntity<StudentDto[]> getStudentsByAgeResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/age/" + age1,
                HttpMethod.GET,
                null,
                StudentDto[].class
                );

        assertThat(getStudentsByAgeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDto[] studentDtoArray = getStudentsByAgeResponse.getBody();
        List<StudentDto> receivedStudentDtoList = Arrays.asList(studentDtoArray);

        assertThat(receivedStudentDtoList).isNotNull();


        for (int i = 0; i < receivedStudentDtoList.size(); i++) {
            StudentDto receivedStudentDto = receivedStudentDtoList.get(i);
            StudentDto initialStudentDto = studentDtoList.get(i);
            assertThat(receivedStudentDto.getId()).isEqualTo(initialStudentDto.getId());
            assertThat(receivedStudentDto.getAge()).isEqualTo(initialStudentDto.getAge());
            assertThat(receivedStudentDto.getName()).isEqualTo(initialStudentDto.getName());
        }


 }

    @Test
    public void findByAgeBetweenTest() {
     Student student2 = new Student();
        student2.setName(name2);
        student2.setAge(age2);

        Student createdStudent1 = studentRepository.save(student1);
        studentRepository.save(student2);

        StudentDto studentDto1 = new StudentDto();
        studentDto1.setId(createdStudent1.getId());
        studentDto1.setName(createdStudent1.getName());
        studentDto1.setAge(createdStudent1.getAge());

        List<StudentDto> studentDtoList = new ArrayList<>();
        studentDtoList.add(studentDto1);

        ResponseEntity<StudentDto[]> findStudentsByAgeBetweenResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/filter?from=16&to=" + age1,
                HttpMethod.GET,
                null,
                StudentDto[].class
        );

        assertThat(findStudentsByAgeBetweenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDto[] studentDtoArray = findStudentsByAgeBetweenResponse.getBody();
        List<StudentDto> receivedStudentDtoList = Arrays.asList(studentDtoArray);

        assertThat(receivedStudentDtoList).isNotNull();


        for (int i = 0; i < receivedStudentDtoList.size(); i++) {
            StudentDto receivedStudentDto = receivedStudentDtoList.get(i);
            StudentDto initialStudentDto = studentDtoList.get(i);
            assertThat(receivedStudentDto.getId()).isEqualTo(initialStudentDto.getId());
            assertThat(receivedStudentDto.getAge()).isEqualTo(initialStudentDto.getAge());
            assertThat(receivedStudentDto.getName()).isEqualTo(initialStudentDto.getName());
        }
    }
    @Test
    public void findFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Test");
        faculty.setColor("Test");

        Faculty createdFaculty = facultyRepository.save(faculty);
        student1.setFaculty(createdFaculty);

        Student createdStudent = studentRepository.save(student1);
        Long studentId = createdStudent.getId();

        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(createdFaculty.getId());
        facultyDto.setName(createdFaculty.getName());
        facultyDto.setColor(createdFaculty.getColor());

        ResponseEntity<FacultyDto> findFacultyResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + studentId + "/faculty",
                HttpMethod.GET,
                null,
                FacultyDto.class
        );

        assertThat(findFacultyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        FacultyDto foundFacultyDto = findFacultyResponse.getBody();

        assertThat(foundFacultyDto).isNotNull();
        assertThat(foundFacultyDto.getId()).isEqualTo(facultyDto.getId());
        assertThat(foundFacultyDto.getColor()).isEqualTo(facultyDto.getColor());
        assertThat(foundFacultyDto.getName()).isEqualTo(facultyDto.getName());

    }



}
