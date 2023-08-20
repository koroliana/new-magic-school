package pro.sky.newmagicschool.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.newmagicschool.entity.Student;

import java.util.List;

public interface StudentRepository  extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findAllByAgeBetween(int ageFrom, int ageTo);

    List<Student> findAllByFaculty_Id(Long facultyId);

    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    Integer countSchoolStudents();

    @Query("SELECT avg(s.age) FROM Student s")
    Double countAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> getLastStudents(Pageable pageable);
}
