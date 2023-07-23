package pro.sky.newmagicschool.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;

    @OneToOne
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;



/*
    public Student(String name, int age) {
        if (age <= 0) {
            throw new IncorrectStudentAgeException(age);
        }
        this.name = name;
        this.age = age;
    }

 */

    public Student() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Avatar getAvatar(){return avatar;}
    public void setAvatar(Avatar avatar) {this.avatar = avatar;}

    public boolean haveAvatar(){
        return avatar != null;
    }

    public void deleteFaculty() {
        this.faculty = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return this.age == student.getAge() && this.name.equals(student.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge());
    }

    @Override
    public String toString() {
        return "id:" + id + " - "
                +"Имя: " + name + ", возраст: " + age;
    }
}
