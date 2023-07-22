package pro.sky.newmagicschool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.newmagicschool.dto.FacultyDto;
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
    public List<FacultyDto> findAll(@RequestParam(required = false) String color) {
        return facultyService.findAll(color);
    }

    @GetMapping("/filter")
    public List<FacultyDto> findByColorOrName(@RequestParam String colorOrName) {
        return facultyService.getFacultyByColorOrName(colorOrName);
    }


    @GetMapping("{id}")
    public ResponseEntity<FacultyDto> getFacultyInfo(@PathVariable Long id) {
        FacultyDto facultyDto = facultyService.findFacultyById(id);
        if (facultyDto==null){
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(facultyDto);
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
    public ResponseEntity<FacultyDto> editFaculty(@RequestBody Faculty faculty) {
        FacultyDto facultyDto = facultyService.updateFaculty(faculty);
        if (facultyDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(facultyDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FacultyDto> deleteFaculty(@PathVariable Long id) {
        FacultyDto facultyDto = facultyService.deleteFaculty(id);
        if (facultyDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else return ResponseEntity.ok(facultyDto);
    }

    @GetMapping("/sum")
    public Integer sum() {
        return facultyService.sum();
    }

    @GetMapping("/sum-impr")
    public Integer sumImpr() {
        return facultyService.sumImpr();
    }



/*
    @ExceptionHandler(Exception.class)
    public String ExceptionHandler(Exception e) {
        return e.getMessage();
    }

 */

}

