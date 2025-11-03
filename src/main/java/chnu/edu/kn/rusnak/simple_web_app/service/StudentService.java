package chnu.edu.kn.rusnak.simple_web_app.service;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    private final List<Student> students = new ArrayList<Student>(
            Arrays.asList(
                    new Student("1", "Lionel", "Messi", 18),
                    new Student("2", "Cristiano", "Ronaldo", 20),
                    new Student("3", "Sergio", "Ramos", 22),
                    new Student("4", "Roberto", "Carlos", 19),
                    new Student("5", "David", "Silva", 19)
            )
    );

    //@PostConstruct
    public void init(){
        studentRepository.saveAll(students);
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student add(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id).get();
    }

    public void deleteStudentById(String id) {
        studentRepository.deleteById(id);
    }

    public Student update(Student student) {
        if (student.getId() == null){
            return null;
        }
        return studentRepository.save(student);
    }
}
