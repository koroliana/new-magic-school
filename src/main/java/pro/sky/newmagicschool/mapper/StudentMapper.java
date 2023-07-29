package pro.sky.newmagicschool.mapper;

import org.springframework.stereotype.Component;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.exception.FacultyNotFoundException;
import pro.sky.newmagicschool.repository.FacultyRepository;

import java.util.Optional;

@Component
public class StudentMapper {

    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;

    public StudentMapper(FacultyMapper facultyMapper, FacultyRepository facultyRepository) {
        this.facultyMapper = facultyMapper;
        this.facultyRepository = facultyRepository;
    }

    public StudentDto toDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setAge(student.getAge());
        Optional.ofNullable(student.getFaculty())
                .ifPresent(faculty -> studentDto.setFacultyId(facultyMapper.toDto(faculty).getId()));
        return studentDto;
    }

    public Student toEntity(StudentDto studentDto){
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setName(studentDto.getName());
        student.setAge(studentDto.getAge());
        Optional.ofNullable(studentDto.getFacultyId())
                .ifPresent(facultyId ->
                        facultyRepository.findById(facultyId)
                                .orElseThrow(()->new FacultyNotFoundException(facultyId))
                );
        return student;

    }
}
