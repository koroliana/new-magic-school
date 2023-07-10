package pro.sky.newmagicschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.newmagicschool.entity.Student;

import java.util.List;

public interface StudentRepository  extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findAllByAgeBetween(int ageFrom, int ageTo);

    List<Student> findAllByFaculty_Id(Long facultyId);
}
