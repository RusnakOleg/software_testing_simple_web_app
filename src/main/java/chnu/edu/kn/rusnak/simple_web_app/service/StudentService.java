package chnu.edu.kn.rusnak.simple_web_app.service;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.repository.StudentRepository;
import chnu.edu.kn.rusnak.simple_web_app.request.StudentCreateRequest;
import chnu.edu.kn.rusnak.simple_web_app.request.StudentUpdateRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Student addStudent(StudentCreateRequest request) {

        if (studentRepository.existsByFirstnameAndLastname(request.firstname(), request.lastname())) {
            throw new IllegalStateException("Student with same name already exists");
        }

        Student student = mapToStudent(request);
        student.setCreatedDate(LocalDateTime.now());
        student.setLastModifiedDate(null);

        return studentRepository.save(student);
    }

    private Student mapToStudent(StudentCreateRequest request) {
        return new Student(request.firstname(), request.lastname(), request.age());
    }

    public Student updateStudent(StudentUpdateRequest request) {

        Student persisted = studentRepository.findById(request.id()).orElse(null);

        if (persisted != null) {

            Student updated = Student.builder()
                    .id(request.id())
                    .firstname(request.firstname())
                    .lastname(request.lastname())
                    .age(request.age())
                    .createdDate(persisted.getCreatedDate())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            return studentRepository.save(updated);
        }
        return null;
    }

}
