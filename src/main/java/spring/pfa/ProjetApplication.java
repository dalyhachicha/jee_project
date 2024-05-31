package spring.pfa;

import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import spring.pfa.model.Administrateur;
import spring.pfa.model.Employe;
import spring.pfa.repository.AdministrateurRepository;
import spring.pfa.repository.CongeRepository;
import spring.pfa.repository.EmployeRepository;

@SpringBootApplication
public class ProjetApplication {
	static AdministrateurRepository adminRepo;
	static CongeRepository congeRepo;
	static EmployeRepository employeRepo;
	

	public static void main(String[] args) {
		ApplicationContext contexte = SpringApplication.run(ProjetApplication.class, args);
		
		// repos
		adminRepo = contexte.getBean(AdministrateurRepository.class);
		congeRepo = contexte.getBean(CongeRepository.class);
		employeRepo = contexte.getBean(EmployeRepository.class);
		
		// créer deux admins
		Administrateur admin1 = new Administrateur(1l ,"admin1@gmail.com", "nom_1", "prenom_1", LocalDate.of(2023, 5, 31), "password");
		Administrateur admin2 = new Administrateur(2l ,"admin2@gmail.com", "nom_2", "prenom_2", LocalDate.of(2023, 5, 31), "password");
		adminRepo.save(admin1);
		adminRepo.save(admin2);
		
		// créer deux employes
		Employe emp1 = new Employe(1l ,"emp1@gmail.com", "nom_1", "prenom_1", LocalDate.of(2023, 5, 31), "password");
		Employe emp2 = new Employe(2l ,"emp2@gmail.com", "nom_2", "prenom_2", LocalDate.of(2023, 5, 31), "password");
		
		// save
		employeRepo.save(emp1);
		employeRepo.save(emp2);
		
		
	}

}
