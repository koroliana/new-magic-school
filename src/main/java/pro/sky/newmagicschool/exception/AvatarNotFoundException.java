package pro.sky.newmagicschool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AvatarNotFoundException extends RuntimeException {

    private final long studentId;

    public AvatarNotFoundException(long studentId) {
        this.studentId = studentId;
    }

    @Override
    public String getMessage() {
        return "Аватар с id студента = " + studentId + " не найден!";
    }
}
