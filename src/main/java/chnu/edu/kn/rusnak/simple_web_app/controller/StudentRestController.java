package chnu.edu.kn.rusnak.simple_web_app.controller;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import chnu.edu.kn.rusnak.simple_web_app.request.StudentCreateRequest;
import chnu.edu.kn.rusnak.simple_web_app.request.StudentUpdateRequest;
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

    //CRUD operation

    @RequestMapping("/")
    public List<Student> getAllStudents(){
        return studentService.getStudents();
    }

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

    @PostMapping("/dto")
    public Student insert(@RequestBody StudentCreateRequest request){
        return studentService.addStudent(request);
    }

    @PutMapping("/dto")
    public Student edit(@RequestBody StudentUpdateRequest request){
        return studentService.updateStudent(request);
    }

}
