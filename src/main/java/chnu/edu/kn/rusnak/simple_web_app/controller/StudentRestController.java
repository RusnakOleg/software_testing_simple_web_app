package chnu.edu.kn.rusnak.simple_web_app.controller;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;

    @RequestMapping("hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/")
    public List<Student> getAllStudents(){
        return studentService.getStudents();
    }

    //CRUD operation

    @PostMapping
    public Student addStudent(@RequestBody Student student){
        return studentService.add(student);
    }

    @RequestMapping("{id}")
    public Student getStudentById(@PathVariable String id){
        return studentService.getStudentById(id);
    }

    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable String id){
        studentService.deleteStudentById(id);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student){
        return studentService.update(student);
    }
}
