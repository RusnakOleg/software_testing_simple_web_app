package chnu.edu.kn.rusnak.simple_web_app;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuditTest {

    @Autowired
    StudentService underTest;

    @BeforeAll
    void setUp() {
        Student lionel = new Student("Lionel", "Messi", 18);
        Student cristiano = new Student("Cristiano", "Ronaldo", 20);
        Student neymar = new Student("Neymar", "Junior", 21);
        underTest.createAll(List.of(lionel, cristiano, neymar));
    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    // 1
    @Test
    void whenCreateNewStudent_AuditFieldsShouldBeFilled() {
        Student student = new Student("Andriy", "Shevchenko", 23);
        Student created = underTest.add(student);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertNotNull(created.getCreatedDate());
        assertNotNull(created.getLastModifiedDate());

        assertEquals(created.getCreatedDate(), created.getLastModifiedDate());
    }

    // 2
    @Test
    void whenUpdateStudent_AuditShouldUpdateLastModified() throws InterruptedException {
        Student student = new Student("Kylian", "Mbappe", 19);
        Student created = underTest.add(student);

        Thread.sleep(50);

        created.setAge(25);

        Student updated = underTest.update(created);

        assertNotNull(updated.getLastModifiedDate());
        assertTrue(updated.getLastModifiedDate().isAfter(created.getCreatedDate()));
    }


    // 3
    @Test
    void createdDateShouldNotChangeAfterUpdate() throws InterruptedException {
        Student st = underTest.add(new Student("Alex", "Test", 22));
        LocalDateTime createdDate = st.getCreatedDate();

        Thread.sleep(30);
        st.setAge(33);

        Student updated = underTest.update(st);

        assertEquals(createdDate, updated.getCreatedDate());
    }

    // 4
    @Test
    void lastModifiedDateShouldIncreaseAfterUpdate() throws InterruptedException {
        Student st = underTest.add(new Student("Kylian", "Mbappe", 19));

        Thread.sleep(40);
        st.setAge(22);

        Student updated = underTest.update(st);

        assertTrue(updated.getLastModifiedDate().isAfter(updated.getCreatedDate()));
    }

    // 5
    @Test
    void shouldCreateMultipleStudents() {
        List<Student> list = List.of(
                new Student("A", "A", 10),
                new Student("B", "B", 20),
                new Student("C", "C", 30)
        );

        int before = underTest.getStudents().size();

        underTest.createAll(list);

        int after = underTest.getStudents().size();

        assertEquals(before + list.size(), after);
    }

    // 6
    @Test
    void shouldDeleteAllStudents() {
        underTest.deleteAll();
        assertEquals(0, underTest.getStudents().size());
    }

    // 7
    @Test
    void shouldUpdateStudentFields() {
        Student st = underTest.add(new Student("John", "Smith", 25));
        st.setAge(35);

        Student updated = underTest.update(st);

        assertEquals(35, updated.getAge());
    }

    // 8
    @Test
    void shouldFindStudentById() {
        Student st = underTest.add(new Student("Find", "Me", 50));

        Student found = underTest.getStudentById(st.getId());

        assertEquals(st.getId(), found.getId());
    }

    // 9
    @Test
    void updateWithoutChangesShouldStillUpdateAudit() throws InterruptedException {
        Student st = underTest.add(new Student("No", "Changes", 40));

        Thread.sleep(40);

        Student updated = underTest.update(st);

        assertTrue(updated.getLastModifiedDate().isAfter(updated.getCreatedDate()));
    }

    // 10
    @Test
    void shouldDeleteStudentById() {
        int before = underTest.getStudents().size();

        Student st = underTest.add(new Student("ToDelete", "User", 33));
        int afterAdd = underTest.getStudents().size();

        underTest.deleteStudentById(st.getId());
        int afterDelete = underTest.getStudents().size();

        assertEquals(before, afterDelete);
    }
}