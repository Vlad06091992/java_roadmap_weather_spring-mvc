package weather_app.dao;

import weather_app.entities.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DAO {

    private final SessionFactory sessionFactory;

    public DAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<Student> getAllStudents(){
        Session session = sessionFactory.getCurrentSession();
        List<Student> students = session.createQuery("from Student", Student.class).list();

//         Получить по ID
//        Student student = session.get(Student.class, 1L);

        // Получить с условием
//        List<Student> students = session.createQuery("from Student where name = :name", Student.class)
//                .setParameter("name", "Иван")
//                .list();
        // ...

        return students;
    }


}