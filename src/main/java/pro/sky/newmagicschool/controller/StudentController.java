package pro.sky.newmagicschool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
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
    public ResponseEntity<List<Student>> getStudentsByAge(@PathVariable int age) {
        List<Student> students = studentService.getStudentsByAge(age);
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(students);
    }

    @GetMapping("/filter")
    public List<Student> findByAgeBetween(@RequestParam int ageFrom, @RequestParam int ageTo) {
        return studentService.getStudentsByAgeBetween(ageFrom, ageTo);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudentById(id);
        if (student==null){
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(student);
    }

    @GetMapping("{id}/faculty")
    public Faculty findFaculty(@PathVariable("id") Long id) {
        return studentService.findFaculty(id);
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
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        Student foundStudent = studentService.deleteStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(foundStudent);
    }

    /*
    @ExceptionHandler(Exception.class)
    public String ExceptionHandler(Exception e) {
        return e.getMessage();
    }

     */

}
