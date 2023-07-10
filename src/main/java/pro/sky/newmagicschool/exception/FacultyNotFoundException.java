package pro.sky.newmagicschool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FacultyNotFoundException extends RuntimeException{
    private final Long facultyId;

    public FacultyNotFoundException(Long facultyId) {
        this.facultyId = facultyId;
    }

    @Override
    public String getMessage() {
        return "Факультет " + facultyId + " не найден";
    }
}
