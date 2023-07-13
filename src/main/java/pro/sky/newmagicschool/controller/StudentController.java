package pro.sky.newmagicschool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.newmagicschool.dto.FacultyDto;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.service.AvatarService;
import pro.sky.newmagicschool.service.StudentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService){
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("/age/{age}")
    public List<StudentDto> getStudentsByAge(@PathVariable int age) {
        List<StudentDto> students = studentService.getStudentsByAge(age);
        return students;
        /*
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(students);

         */
    }

    /*
    @GetMapping("/age")
    public Double getAverageStudentAge() {
        return studentService.getAverageStudentAge();
    }

     */

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


    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student newStudent = studentService.createStudent(student);
        if (newStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(newStudent);
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024*300) { //ограничение в 300Кб
            return ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();

    }

    @PutMapping
    public StudentDto editStudent(@RequestBody StudentDto student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<StudentDto> deleteStudent(@PathVariable Long id) {
        StudentDto studentDto = studentService.deleteStudent(id);
        if (studentDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(studentDto);
    }

    /*
    @ExceptionHandler(Exception.class)
    public String ExceptionHandler(Exception e) {
        return e.getMessage();
    }

     */



}
