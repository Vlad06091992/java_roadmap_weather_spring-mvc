package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
public class StudentController {

    @GetMapping(value = "/students", produces = "text/html;charset=UTF-8")
    public String getStudents(Model model) {
        // Создаем список студентов
        List<String> students = Arrays.asList("Иван Иванов", "Мария Петрова", "Алексей Сидоров");

        // Добавляем список в модель
        model.addAttribute("students", students);

        // Возвращаем имя шаблона (_students.html)
        return "_students";
    }

    @GetMapping("/test")
    public String getTest() {
        return "test";
    }


    @GetMapping("/test2")
    @ResponseBody
    public String getTest2() {
        return "тест 2";
    }
}