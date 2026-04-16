package weather_app.services;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weather_app.dao.DAO;
import weather_app.entities.Student;

import java.util.List;

@Service
@Transactional
public class StudentsService {

    private final DAO dao;

    public StudentsService(DAO dao) {
        this.dao = dao;
    }

    public List<Student> getAllStudents(){
      return this.dao.getAllStudents();
    }
}
