package pro.sky.newmagicschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.newmagicschool.entity.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColor(String color);
    List<Faculty> findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(String color, String name);

}
