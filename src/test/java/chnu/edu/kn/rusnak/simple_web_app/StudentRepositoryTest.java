package chnu.edu.kn.rusnak.simple_web_app;


import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class StudentRepositoryTest {

    @Autowired
    StudentRepository underTest;

    @BeforeEach
    void setUp() {
        Student lionel = new Student("Lionel", "Messi", 18);
        Student cristiano = new Student("Cristiano", "Ronaldo", 20);
        Student neymar = new Student("Neymar", "Junior", 21);
        underTest.saveAll(List.of(lionel, cristiano, neymar));
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    // 1. Перевірка кількості записів
    @Test
    void testShouldContain3Students() {
        List<Student> students = underTest.findAll();
        assertEquals(3, students.size());
    }

    // 2. Перевірка автоматичного присвоєння id
    @Test
    void shouldGiveIdForNewStudent() {
        Student luka = new Student("Luka", "Modric", 22);
        underTest.save(luka);

        Student studentFromDb = underTest.findAll().stream()
                .filter(s -> s.getFirstname().equals("Luka"))
                .findFirst().orElse(null);

        assertNotNull(studentFromDb);
        assertNotNull(studentFromDb.getId());
        assertFalse(studentFromDb.getId().isEmpty());
    }

    // 3. Збереження студента з вручну заданим id
    @Test
    void whenStudentHasIdThenItIsPossibleToSave() {
        Student student = new Student("Kylian", "Mbappe", 19);
        student.setId("customId123456789012345678");
        underTest.save(student);

        Student fromDb = underTest.findAll().stream()
                .filter(s -> s.getId().equals("customId123456789012345678"))
                .findFirst().orElse(null);

        assertNotNull(fromDb);
    }

    // 4. Пошук студента за іменем
    @Test
    void shouldFindStudentByFirstname() {
        List<Student> students = underTest.findAll().stream()
                .filter(s -> s.getFirstname().equals("Lionel"))
                .toList();

        assertEquals(1, students.size());
        assertEquals("Messi", students.get(0).getLastname());
    }

    // 5. Пошук студента за прізвищем
    @Test
    void shouldFindStudentByLastname() {
        List<Student> students = underTest.findAll().stream()
                .filter(s -> s.getLastname().equals("Ronaldo"))
                .toList();

        assertEquals(1, students.size());
        assertEquals("Cristiano", students.get(0).getFirstname());
    }

    // 6. Пошук студентів за віком більше 19
    @Test
    void shouldFindStudentsOlderThan19() {
        List<Student> students = underTest.findAll().stream()
                .filter(s -> s.getAge() > 19)
                .toList();

        assertEquals(2, students.size());
    }

    // 7. Оновлення запису
    @Test
    void shouldUpdateStudentLastname() {
        Student student = underTest.findAll().stream()
                .filter(s -> s.getFirstname().equals("Neymar"))
                .findFirst().orElse(null);

        assertNotNull(student);
        student.setLastname("Da Silva");
        underTest.save(student);

        Student updated = underTest.findAll().stream()
                .filter(s -> s.getFirstname().equals("Neymar"))
                .findFirst().orElse(null);

        assertEquals("Da Silva", updated.getLastname());
    }

    // 8. Видалення студента
    @Test
    void shouldDeleteStudent() {
        Student student = underTest.findAll().stream()
                .filter(s -> s.getFirstname().equals("Cristiano"))
                .findFirst().orElse(null);

        underTest.delete(student);

        List<Student> students = underTest.findAll();
        assertEquals(2, students.size());
        assertFalse(students.stream().anyMatch(s -> s.getFirstname().equals("Cristiano")));
    }

    // 9. Перевірка, що можна зберегти кілька студентів одразу
    @Test
    void shouldSaveMultipleStudents() {
        Student a = new Student("Erling", "Haaland", 21);
        Student b = new Student("Harry", "Kane", 23);
        underTest.saveAll(List.of(a, b));

        List<Student> students = underTest.findAll();
        assertEquals(5, students.size());
    }

    // 10. Пошук за частковим прізвищем
    @Test
    void shouldFindStudentByLastnameContains() {
        List<Student> students = underTest.findAll().stream()
                .filter(s -> s.getLastname().contains("essi"))
                .toList();

        assertEquals(1, students.size());
        assertEquals("Lionel", students.get(0).getFirstname());
    }
}