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
                    new Student( "Lionel", "Messi", 19),
                    new Student( "Cristiano", "Ronaldo", 20),
                    new Student( "Sergio", "Ramos", 22),
                    new Student( "Roberto", "Carlos", 19),
                    new Student( "David", "Silva", 19)
            )
    );

    @PostConstruct
    public void init(){
        studentRepository.deleteAll();
        studentRepository.saveAll(students);
    }

    public List<Student> createAll(List<Student> students){
        return studentRepository.saveAll(students);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student add(Student student) {
        if (student.getId() != null && studentRepository.existsById(student.getId())) {
            return null;
        }
        return studentRepository.save(student);
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id).get();
    }

    public void deleteStudentById(String id) {
        studentRepository.deleteById(id);
    }

    public Student update(Student student) {
        if (student.getId() == null || !studentRepository.existsById(student.getId())){
            return null;
        }
        return studentRepository.save(student);
    }
}
