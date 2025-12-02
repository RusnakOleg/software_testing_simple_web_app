package chnu.edu.kn.rusnak.simple_web_app;

import chnu.edu.kn.rusnak.simple_web_app.Utils.Utils;
import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.repository.StudentRepository;
import chnu.edu.kn.rusnak.simple_web_app.request.StudentCreateRequest;
import chnu.edu.kn.rusnak.simple_web_app.request.StudentUpdateRequest;
import chnu.edu.kn.rusnak.simple_web_app.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(new Student("Lionel", "Messi", 19));
        repository.save(new Student("Cristiano", "Ronaldo", 20));
        repository.save(new Student("Sergio", "Ramos", 22));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    // 1
    @Test
    void whenStudentIsUniqueThenShouldCreateNewStudent() throws Exception {

        StudentCreateRequest req =
                new StudentCreateRequest("Kylian", "Mbappe", 24);

        mockMvc.perform(
                post("/api/v1/students/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(req))
        );

        Student saved = repository.findAll().stream()
                .filter(s -> s.getFirstname().equals("Kylian")
                        && s.getLastname().equals("Mbappe"))
                .findFirst()
                .orElse(null);

        assertNotNull(saved);
        assertEquals(24, saved.getAge());
    }

    // 2
    @Test
    void whenUpdateStudentExistsThenShouldUpdate() throws Exception {

        Student persisted = repository.findAll().get(0);

        StudentUpdateRequest req =
                new StudentUpdateRequest(persisted.getId(), "Lionel", "Messi", 30);

        mockMvc.perform(
                put("/api/v1/students/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(req))
        );

        Student updated = repository.findById(persisted.getId()).orElse(null);

        assertNotNull(updated);
        assertEquals(30, updated.getAge());
    }

    // 3
    @Test
    void whenUpdateStudentIdNotFoundThenShouldReturnNull() throws Exception {

        long before = repository.count();

        StudentUpdateRequest req =
                new StudentUpdateRequest("doesNotExist", "Foo", "Bar", 55);

        mockMvc.perform(
                put("/api/v1/students/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(req))
        );

        long after = repository.count();

        assertEquals(before, after);
    }

    // 4
    @Test
    void whenGetStudentByIdThenReturnIt() throws Exception {

        Student s = repository.findAll().get(1);

        MvcResult result = mockMvc.perform(
                get("/api/v1/students/" + s.getId())
        ).andReturn();

        String json = result.getResponse().getContentAsString();

        assertTrue(json.contains(s.getFirstname()));
        assertTrue(json.contains(s.getLastname()));
    }

    // 5
    @Test
    void whenDeleteStudentByIdThenShouldRemove() throws Exception {

        Student s = repository.findAll().get(0);

        mockMvc.perform(
                delete("/api/v1/students/" + s.getId())
        );

        assertFalse(repository.existsById(s.getId()));
    }

    // 6
    @Test
    void whenDeleteStudentIdNotFoundStill200() throws Exception {

        long before = repository.count();

        mockMvc.perform(
                delete("/api/v1/students/unknown123")
        );

        long after = repository.count();
        assertEquals(before, after);
    }

    // 7
    @Test
    void whenCreateRawStudentThenShouldWork() throws Exception {

        Student st = new Student("Test", "Student", 99);

        mockMvc.perform(
                post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(st))
        );

        boolean exists = repository.findAll().stream()
                .anyMatch(s -> s.getFirstname().equals("Test")
                        && s.getLastname().equals("Student"));

        assertTrue(exists);
    }

    // 8
    @Test
    void whenCreateAllThenStudentsAreAdded() {
        long before = repository.count();

        List<Student> newStudents = List.of(
                new Student("Test1", "User1", 18),
                new Student("Test2", "User2", 19),
                new Student("Test3", "User3", 20)
        );

        studentService.createAll(newStudents);

        long after = repository.count();

        assertEquals(before + newStudents.size(), after);
    }

    // 9
    @Test
    void whenDeleteAllThenNoStudentsRemain() {
        assertTrue(repository.count() > 0);

        studentService.deleteAll();

        assertEquals(0, repository.count());
    }

    // 10
    @Test
    void whenUpdateStudentWithoutIdThenReturnNullAndNoChanges() {
        long before = repository.count();

        Student studentWithoutId = new Student("NoId", "Student", 25);

        Student result = studentService.update(studentWithoutId);

        assertNull(result);

        long after = repository.count();
        assertEquals(before, after);

        boolean exists = repository.findAll().stream()
                .anyMatch(s -> "NoId".equals(s.getFirstname()) &&
                        "Student".equals(s.getLastname()));
        assertFalse(exists);
    }
}