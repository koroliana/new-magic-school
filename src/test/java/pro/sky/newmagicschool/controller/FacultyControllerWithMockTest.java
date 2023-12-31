package pro.sky.newmagicschool.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.newmagicschool.dto.FacultyDto;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Faculty;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.mapper.FacultyMapper;
import pro.sky.newmagicschool.mapper.StudentMapper;
import pro.sky.newmagicschool.repository.AvatarRepository;
import pro.sky.newmagicschool.repository.FacultyRepository;
import pro.sky.newmagicschool.repository.StudentRepository;
import pro.sky.newmagicschool.service.AvatarService;
import pro.sky.newmagicschool.service.FacultyService;
import pro.sky.newmagicschool.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest
public class FacultyControllerWithMockTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private AvatarService avatarService;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private StudentMapper studentMapper;

    @SpyBean
    private FacultyMapper facultyMapper;

    @InjectMocks
    private FacultyController facultyController;

    private final Faker faker = new Faker();

    Long id = 1L;
    String name = "Wolvenwood";
    String color = "silver";
    Faculty faculty = new Faculty();
    //FacultyDto facultyDto = new FacultyDto();

    @BeforeEach
    public void beforeEach() {
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
/*
        facultyDto.setId(faculty.getId());
        facultyDto.setName(faculty.getName());
        facultyDto.setColor(faculty.getColor());

 */

    }


    @Test
    public void createFacultyTest() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", faculty.getId());
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty receivedFaculty = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            Faculty.class
                    );
                    assertThat(receivedFaculty).isNotNull();
                    assertThat(receivedFaculty.getId()).isEqualTo(1L);
                    assertThat(receivedFaculty.getColor()).isEqualTo(faculty.getColor());
                    assertThat(receivedFaculty.getName()).isEqualTo(faculty.getName());
                });
        verify(facultyRepository, times(1)).save(any());
                /*
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

                 */


    }

    @Test
    public void editFacultyTest() throws Exception {
        String oldName = "Durmstrang";
        String oldColor = "scarlet";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName(oldName);
        oldFaculty.setColor(oldColor);

        when(facultyRepository.findById(eq(id))).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculty")
                                .content(facultyObject.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value(name))
                    .andExpect(jsonPath("$.color").value(color));
        verify(facultyRepository, Mockito.times(1)).save(any());

        Mockito.reset(facultyRepository);

        //facultyObject.put("id", 2L); - данная строчка роли не играет
        when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculty")
                                .content(facultyObject.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound()) ;
        verify(facultyRepository, never()).save(any());
    }

    @Test
    public void deleteFacultyTest() throws Exception {

        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty receivedFaculty = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            Faculty.class
                    );
                    assertThat(receivedFaculty).isNotNull();
                    assertThat(receivedFaculty.getId()).isEqualTo(1L);
                    assertThat(receivedFaculty.getColor()).isEqualTo(faculty.getColor());
                    assertThat(receivedFaculty.getName()).isEqualTo(faculty.getName());
                });

                /*
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

                 */

        verify(facultyRepository, times(1)).delete(any());

        Mockito.reset(facultyRepository);

        when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/2"))
                    .andExpect(status().isNotFound()) ;
        verify(facultyRepository, never()).delete(any());

    }

    @Test
    public void getFacultyInfoTest() throws Exception {

        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyRepository, times(1)).findById(any());

        Mockito.reset(facultyRepository);

        when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/2"))
                .andExpect(status().isNotFound()) ;
        verify(facultyRepository, times(1)).findById(any());

    }

    @Test
    public void findAllTest() throws Exception {
        List<Faculty> faculties = Stream.iterate(2, id -> id + 1)
                .map(this::generate)
                .limit(20)
                .collect(Collectors.toList());

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(result ->
                        {
                            try {
                                List<FacultyDto> receivedFaculties = objectMapper
                                        .readValue(
                                                result.getResponse().getContentAsString(),
                                                new TypeReference<List<FacultyDto>>() {
                                                }
                                        );
                                assertThat(receivedFaculties)
                                        .isNotNull()
                                        .isNotEmpty();
                                Stream.iterate(0, index -> index + 1)
                                        .limit(receivedFaculties.size())
                                        .forEach(index ->
                                        {
                                            FacultyDto receivedFacultyDto = receivedFaculties.get(index);
                                            Faculty expected = faculties.get(index);
                                            assertThat(receivedFacultyDto.getId()).isEqualTo(expected.getId());
                                            assertThat(receivedFacultyDto.getColor()).isEqualTo(expected.getColor());
                                            assertThat(receivedFacultyDto.getName()).isEqualTo(expected.getName());
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
        verify(facultyRepository, times(1)).findAll();

        faculties.add(1, faculty);

        List<Faculty> facultiesOfSearchedColor = faculties.stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toList());

        when(facultyRepository.findAllByColor(eq(color))).thenReturn(facultiesOfSearchedColor);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty?color=" + color)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect( result ->
                        {
                            try {
                                List<FacultyDto> receivedFaculties = objectMapper
                                        .readValue(
                                                result.getResponse().getContentAsString(),
                                                new TypeReference<List<FacultyDto>>() {}
                                        );
                                assertThat(receivedFaculties)
                                        .isNotNull()
                                        .isNotEmpty();
                                Stream.iterate(0, index -> index + 1)
                                        .limit(receivedFaculties.size())
                                        .forEach(index ->
                                        {
                                            FacultyDto receivedFacultyDto = receivedFaculties.get(index);
                                            Faculty expected = facultiesOfSearchedColor.get(index);
                                            assertThat(receivedFacultyDto.getId()).isEqualTo(expected.getId());
                                            assertThat(receivedFacultyDto.getColor()).isEqualTo(expected.getColor());
                                            assertThat(receivedFacultyDto.getName()).isEqualTo(expected.getName());
                                        });
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );

        verify(facultyRepository, times(1)).findAllByColor(any());
    }

    @Test
    public void findByColorOrNameTest() throws Exception {
        List<Faculty> faculties = Stream.iterate(2, id -> id + 1)
                .map(this::generate)
                .limit(20)
                .collect(Collectors.toList());

        String searchableString = "wood";

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Test");
        faculty2.setColor(searchableString);

        faculties.add(1, faculty);
        faculties.add(2,faculty2);

        List<Faculty> facultiesOfSearchedColorOrName = faculties.stream()
                .filter(f -> f.getColor().contains(searchableString)||f.getName().contains(searchableString))
                .collect(Collectors.toList());

        when(facultyRepository
                .findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(
                        eq(searchableString),eq(searchableString)))
                .thenReturn(facultiesOfSearchedColorOrName);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/filter?colorOrName=" + searchableString)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect( result ->
                        {
                            try {
                                List<FacultyDto> receivedFaculties = objectMapper
                                        .readValue(
                                                result.getResponse().getContentAsString(),
                                                new TypeReference<List<FacultyDto>>() {}
                                        );
                                assertThat(receivedFaculties)
                                        .isNotNull()
                                        .isNotEmpty();
                                Stream.iterate(0, index -> index + 1)
                                        .limit(receivedFaculties.size())
                                        .forEach(index ->
                                        {
                                            FacultyDto receivedFacultyDto = receivedFaculties.get(index);
                                            Faculty expected = facultiesOfSearchedColorOrName.get(index);
                                            assertThat(receivedFacultyDto.getId()).isEqualTo(expected.getId());
                                            assertThat(receivedFacultyDto.getColor()).isEqualTo(expected.getColor());
                                            assertThat(receivedFacultyDto.getName()).isEqualTo(expected.getName());
                                        });
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );

        verify(facultyRepository, times(1))
                .findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(
                        any(),any());
    }

    @Test
    public void findStudentsTest() throws Exception {
        List<Student> students = Stream.iterate(2, id -> id + 1)
                .map(this::generateStudents)
                .limit(5)
                .collect(Collectors.toList());

        when(studentRepository.findAllByFaculty_Id(eq(id))).thenReturn(students);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/"+ id +"/students")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect( result ->
                        {
                            try {
                                List<StudentDto> receivedStudents = objectMapper
                                        .readValue(
                                                result.getResponse().getContentAsString(),
                                                new TypeReference<List<StudentDto>>() {}
                                        );
                                assertThat(receivedStudents)
                                        .isNotNull()
                                        .isNotEmpty();
                                Stream.iterate(0, index -> index + 1)
                                        .limit(receivedStudents.size())
                                        .forEach(index ->
                                        {
                                            StudentDto receivedStudentDto = receivedStudents.get(index);
                                            Student expected = students.get(index);
                                            assertThat(receivedStudentDto.getId()).isEqualTo(expected.getId());
                                            assertThat(receivedStudentDto.getAge()).isEqualTo(expected.getAge());
                                            assertThat(receivedStudentDto.getName()).isEqualTo(expected.getName());
                                        });
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );

        verify(studentRepository, times(1)).findAllByFaculty_Id(any());

    }

    private Faculty generate(long id) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(faker.animal().name());
        faculty.setColor(faker.color().name());
        return faculty;
    }

    private Student generateStudents(long id) {
        Student student = new Student();
        student.setId(id);
        student.setName(faker.harryPotter().character());
        student.setAge((int) Math.pow(Math.random(), 2));
        student.setFaculty(faculty);
        return student;
    }
}
