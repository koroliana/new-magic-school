package pro.sky.newmagicschool.dto;

public class StudentDto {
    private long id;
    private String name;
    private int age;
    // private FacultyDtoIn faculty;
    private long facultyId;
    private String avatarUrl;


    public StudentDto(Long id, String name, int age, long facultyId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.facultyId = facultyId;
    }

    public StudentDto() {

    }



    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
/*
    public FacultyDtoIn getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDtoIn faculty) {
        this.faculty = faculty;
    }

 */

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId=facultyId;

    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


}
