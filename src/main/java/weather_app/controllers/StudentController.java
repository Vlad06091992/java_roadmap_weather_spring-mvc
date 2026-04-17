package weather_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weather_app.entities.Student;
import weather_app.networkAdapter.WeatherData;
import weather_app.services.StudentsService;

import java.util.Arrays;
import java.util.List;

@Controller
public class StudentController {
    private final StudentsService studentsService;
    private final WeatherData weatherData;

    public StudentController(StudentsService studentsService, WeatherData weatherData) {
        this.studentsService = studentsService;
        this.weatherData = weatherData;
    }


    @GetMapping(value = "/students", produces = "text/html;charset=UTF-8")
    public String getStudents(Model model) {

        List<Student> st = studentsService.getAllStudents();
        // Создаем список студентов
        List<String> students = Arrays.asList("Иван Иванов", "Мария Петрова", "Алексей Сидоров");

        // Добавляем список в модель
        model.addAttribute("students", students);

        // Возвращаем имя шаблона (_students.html)
        return "students";
    }

    @GetMapping("/test")
    public String getTest() {
        return "test";
    }


    //отдаст JSON
    @GetMapping(path = "/test2", produces = "application/json")
    @ResponseBody
    public String getTest2() {
        return weatherData.getWeatherString();
    }


    //отдаст строку
    @GetMapping("/test3")
    @ResponseBody
    public String getTest3() {
        return weatherData.getWeatherString();
    }
}