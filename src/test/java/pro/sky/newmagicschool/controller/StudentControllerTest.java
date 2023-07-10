package pro.sky.newmagicschool.controller;

import org.assertj.core.api.Assertions;
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
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.repository.StudentRepository;

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
    String name1 = "Viktor Krum";
    int age1 = 18;

    String name2 = "Виктор Крам";
    int age2 = 19;
    Student student1 = new Student();


    @BeforeEach
    public void beforeEach() {
        student1.setName(name1);
        student1.setAge(age1);

    }
/*

    @AfterEach
    public void clean() {
        studentRepository.deleteAll();
    }

 */

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

        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setName(name2);
        studentDto.setAge(age2);

        long incorrectId = 111;

        ResponseEntity<String> stringResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/students/" + incorrectId,
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                String.class
        );
        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


    @Test
    public void editStudentTest() {

        /*

        ResponseEntity<Student> responseEntity1 = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student1,
                Student.class
        );

        long id = responseEntity1.getBody().getId();

         */

        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setName(name2);
        studentDto.setAge(age2);
        studentDto.setFacultyId(4L);

        ResponseEntity<StudentDto> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        /*

        StudentDto studentDto2 = responseEntity2.getBody();

        assertThat(studentDto2).isNotNull();
        assertThat(studentDto2.getId()).isEqualTo(id);
        assertThat(studentDto2.getAge()).isEqualTo(studentDto.getAge());
        assertThat(studentDto2.getName()).isEqualTo(studentDto.getName());

         */

    }

}
