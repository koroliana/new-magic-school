package pro.sky.newmagicschool.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "faculty")
    private List<Student> facultyStudents;

    /*
    public Faculty(String name, String color) {
        this.name = name;
        this.color = color;
    }

     */

    public Faculty() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Student> getFacultyStudents() {
        return facultyStudents;
    }

    public void setFacultyStudents(List<Student> facultyStudents) {
        this.facultyStudents = facultyStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(getName(), faculty.getName()) && Objects.equals(getColor(), faculty.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getColor());
    }

    @Override
    public String toString() {
        return "Факультет " + name + ", цвет: " + color;
    }

}
