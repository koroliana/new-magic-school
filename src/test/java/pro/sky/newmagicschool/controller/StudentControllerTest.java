package pro.sky.newmagicschool.controller;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.newmagicschool.JsonUtil;
import pro.sky.newmagicschool.StudentDtoList;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.repository.FacultyRepository;
import pro.sky.newmagicschool.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

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

        Student createdStudent = studentRepository.save(student1);

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

 /*
        @GetMapping("/age/{age}")
    public ResponseEntity<List<StudentDto>> getStudentsByAge(@PathVariable int age) {
        List<StudentDto> students = studentService.getStudentsByAge(age);
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(students);
    }

  */
    @Test
    public void getStudentsByAgeTest() {
        /*
        Faculty faculty = new Faculty();
        faculty.setName("Test");
        faculty.setColor("Test");

        Faculty createdFaculty = facultyRepository.save(faculty);

        student1.setFaculty(createdFaculty);

         */

        Student student2 = new Student();
        student2.setName(name2);
        student2.setAge(age1);
       // student2.setFaculty(createdFaculty);

        Student createdStudent1 = studentRepository.save(student1);
        Student createdStudent2 = studentRepository.save(student2);

        StudentDto studentDto1 = new StudentDto();
        studentDto1.setId(createdStudent1.getId());
        studentDto1.setName(createdStudent1.getName());
        studentDto1.setAge(createdStudent1.getAge());
     //   studentDto1.setFacultyId(createdStudent1.getFaculty().getId());

        StudentDto studentDto2 = new StudentDto();
        studentDto2.setId(createdStudent2.getId());
        studentDto2.setName(createdStudent2.getName());
        studentDto2.setAge(createdStudent2.getAge());
     //   studentDto2.setFacultyId(createdStudent2.getFaculty().getId());

        List<StudentDto> studentDtoList = new ArrayList<>();
        studentDtoList.add(studentDto1);
        studentDtoList.add(studentDto2);


        ResponseEntity<StudentDtoList> getStudentsByAgeResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/age/" + age1,
                HttpMethod.GET,
              //  new HttpEntity<>(""),
                null,
                StudentDtoList.class
                );

        assertThat(getStudentsByAgeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        /*
        Gson g = new Gson();
        StudentDtoList receivedStudentDtoList = g.fromJson(getStudentsByAgeResponse.getBody(), StudentDtoList.class);

         */

        StudentDtoList receivedStudentDtoList = getStudentsByAgeResponse.getBody();


        assertThat(receivedStudentDtoList.matchesCount(2));

         assertThat(receivedStudentDtoList).isNotNull();


        for (int i = 0; i < receivedStudentDtoList.getCount(); i++) {
            StudentDto receivedStudentDto = receivedStudentDtoList.getStudentDto(i);
            StudentDto initialStudentDto = studentDtoList.get(i);
            assertThat(receivedStudentDto.getId()).isEqualTo(initialStudentDto.getId());
            assertThat(receivedStudentDto.getAge()).isEqualTo(initialStudentDto.getAge());
            assertThat(receivedStudentDto.getName()).isEqualTo(initialStudentDto.getName());
        }


 }


 /*
    @GetMapping("/filter")
    public List<StudentDto> findByAgeBetween(@RequestParam int from, @RequestParam int to) {
        return studentService.getStudentsByAgeBetween(from, to);
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentDto> getStudentInfo(@PathVariable Long id) {
        StudentDto studentDto = studentService.findStudentById(id);
        if (studentDto==null){
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(studentDto);
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<FacultyDto> findFaculty(@PathVariable("id") Long id) {
        FacultyDto facultyDto = studentService.findFaculty(id);
        if (facultyDto==null){
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(facultyDto);
    }
     */
}
