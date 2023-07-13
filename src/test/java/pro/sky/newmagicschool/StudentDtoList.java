package pro.sky.newmagicschool;

import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Student;

import java.util.List;

public class StudentDtoList {
    private List<StudentDto> students;
    private int count;

    public StudentDtoList(List<StudentDto> students) {
        this.students = students;
        this.count = students.size();
    }

    public StudentDtoList(){
    }

    public List<StudentDto> getStudentDtoList() {
        return students;
    }

    public StudentDto getStudentDto(int id){
        return students.get(id);
    }


    public int getCount() {
        return count;
    }

    public boolean matchesCount(int countToCheck) {
        return count == countToCheck;
    }
}
