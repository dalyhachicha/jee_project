package spring.pfa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.pfa.model.Administrateur;
import spring.pfa.model.Employe;
import spring.pfa.repository.AdministrateurRepository;
import spring.pfa.repository.EmployeRepository;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private AdministrateurRepository adminRepository;
    
    @RequestMapping(value="/login")
    public String login() {
    	return "login";
    }
    
    @PostMapping(value="/login")
    public String login(@RequestParam("email") String email, 
                        @RequestParam("password") String password, 
                        @RequestParam("role") String role, 
                        Model model) {
        if ("admin".equalsIgnoreCase(role)) {
            return adminLogin(email, password, model);
        } else {
            return employeeLogin(email, password, model);
        }
    }

    public String adminLogin(String email, String password, Model model) {
        Administrateur admin = adminRepository.findByEmailAndPassword(email, password);
        System.out.println();
        if (admin != null) {
            model.addAttribute("user", admin);
            return "redirect:/admin/index";
        } else {
            model.addAttribute("error", "Invalid credentials for admin");
            return "login";
        }
    }

    public String employeeLogin(String email, String password, Model model) {
        Employe employee = employeRepository.findByEmailAndPassword(email, password);
        if (employee != null) {
            model.addAttribute("user", employee);
            return "redirect:/employe/index";
        } else {
            model.addAttribute("error", "Invalid credentials for employee");
            return "login";
        }
    }
}
