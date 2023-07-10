package pro.sky.newmagicschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.newmagicschool.entity.Avatar;

import java.util.Optional;

public interface AvatarRepository  extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(long studentId);
}
