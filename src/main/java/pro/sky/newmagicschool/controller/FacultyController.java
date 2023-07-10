package pro.sky.newmagicschool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService){
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<Faculty> findAll(@RequestParam(required = false) String color) {
        return facultyService.findAll(color);
        /*
        List <Faculty> faculties = facultyService.getFacultyByColor(color);
        if (faculties == null) {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(faculties);

         */
    }

    @GetMapping("/filter")
    public List<Faculty> findByColorOrName(@RequestParam String colorOrName) {
        return facultyService.getFacultyByColorOrName(colorOrName);
    }


    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFacultyById(id);
        if (faculty==null){
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(faculty);
    }

    @GetMapping("{id}/students")
    public List<StudentDto> findStudents(@PathVariable("id") Long id) {
        return facultyService.findStudents(id);
    }


    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.updateFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        Faculty foundFaculty = facultyService.deleteFaculty(id);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(foundFaculty);
    }
/*
    @ExceptionHandler(Exception.class)
    public String ExceptionHandler(Exception e) {
        return e.getMessage();
    }

 */

}

