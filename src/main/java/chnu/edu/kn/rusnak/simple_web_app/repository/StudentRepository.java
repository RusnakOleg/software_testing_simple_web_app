package chnu.edu.kn.rusnak.simple_web_app.repository;

import chnu.edu.kn.rusnak.simple_web_app.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    boolean existsByFirstnameAndLastname(String firstname, String lastname);
}
