package pro.sky.newmagicschool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {
    private final Long studentId;

    public StudentNotFoundException(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public String getMessage() {
        return "Студент " + studentId + " не найден";
    }

}
