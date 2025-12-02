package chnu.edu.kn.rusnak.simple_web_app;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.repository.StudentRepository;
import chnu.edu.kn.rusnak.simple_web_app.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceMockTest {

    @Mock
    private StudentRepository mockRepository;

    @InjectMocks
    private StudentService underTest;

    // 1
    @Test
    void whenAddStudentAndIdNotExistsThenOk() {
        Student st = new Student("Cristiano", "Ronaldo", 35);
        st.setId("10");

        given(mockRepository.existsById("10")).willReturn(false);
        given(mockRepository.save(st)).willReturn(st);

        Student saved = underTest.add(st);

        assertNotNull(saved);
        assertEquals(st, saved);

        verify(mockRepository).existsById("10");
        verify(mockRepository).save(st);
    }

    // 2
    @Test
    void whenAddStudentWithNullIdThenSave() {
        Student st = new Student("Messi", "Junior", 18);

        given(mockRepository.save(st)).willReturn(st);

        Student saved = underTest.add(st);

        assertNotNull(saved);
        verify(mockRepository, never()).existsById(any());
        verify(mockRepository).save(st);
    }

    // 3
    @Test
    void whenUpdateStudentWithNullIdThenFail() {
        Student st = new Student("Neymar", "Junior", 25);

        Student updated = underTest.update(st);

        assertNull(updated);
        verify(mockRepository, never()).existsById(any());
        verify(mockRepository, never()).save(any());
    }

    // 4
    @Test
    void whenUpdateStudentWithNotExistingIdThenFail() {
        Student st = new Student("Sergio", "Ramos", 34);
        st.setId("99");

        given(mockRepository.existsById("99")).willReturn(false);

        Student updated = underTest.update(st);

        assertNull(updated);
        verify(mockRepository).existsById("99");
        verify(mockRepository, never()).save(any());
    }

    // 5
    @Test
    void whenCreateAllThenSaveAllCalled() {
        List<Student> list = List.of(
                new Student("A", "A", 10),
                new Student("B", "B", 20)
        );

        given(mockRepository.saveAll(list)).willReturn(list);

        List<Student> saved = underTest.createAll(list);

        assertEquals(2, saved.size());
        verify(mockRepository).saveAll(list);
    }
}

