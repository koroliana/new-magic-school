package pro.sky.newmagicschool.mapper;

import org.springframework.stereotype.Component;
import pro.sky.newmagicschool.dto.FacultyDto;
import pro.sky.newmagicschool.entity.Faculty;

@Component
public class FacultyMapper {

    public FacultyDto toDto(Faculty faculty) {
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(faculty.getId());
        facultyDto.setName(faculty.getName());
        facultyDto.setColor(faculty.getColor());
        return facultyDto;
    }

    public Faculty toEntity(FacultyDto facultyDto) {
        Faculty faculty = new Faculty();
        faculty.setId(facultyDto.getId());
        faculty.setName(facultyDto.getName());
        faculty.setColor(facultyDto.getColor());
        return faculty;
    }

}
