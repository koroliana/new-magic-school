package pro.sky.newmagicschool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectStudentAgeException extends RuntimeException {
    private final int ageFrom;
    private final int ageTo;

    public IncorrectStudentAgeException(int age) {

        this.ageFrom = age;
        this.ageTo = age;

    }
    public IncorrectStudentAgeException(int ageFrom, int ageTo) {

        this.ageFrom = ageFrom;
        this.ageTo = ageTo;

    }

    @Override
    public String getMessage() {
        if(ageTo==ageFrom) {
            return "Возраст " + ageTo + "не может быть меньше или равен нулю";
        }
        else return "Указанный возраст от: " + ageFrom + " больше указанного возраста до: " + ageTo;

    }
}
