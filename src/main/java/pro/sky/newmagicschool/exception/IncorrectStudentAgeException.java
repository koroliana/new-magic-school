package pro.sky.newmagicschool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectStudentAgeException extends RuntimeException {
    private final int age;

    public IncorrectStudentAgeException(int age) {
        this.age = age;
    }

    @Override
    public String getMessage() {
        return "Возраст " + age + "не может быть меньше или равен нулю";
    }
}
