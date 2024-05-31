package spring.pfa.controller;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import spring.pfa.model.Conge;
import spring.pfa.model.Employe;
import spring.pfa.repository.CongeRepository;
import spring.pfa.repository.EmployeRepository;

@Controller
@RequestMapping(value = "/employe")
public class EmployeController {

	private final CongeRepository congeRepos;
	private final EmployeRepository employeRepos;

	@Autowired
	public EmployeController(CongeRepository congeRepos, EmployeRepository employeRepos) {
		this.congeRepos = congeRepos;
		this.employeRepos = employeRepos;
	}

	@RequestMapping(value = "/index")
	public String index(Model model, @RequestParam(name = "id") Long id,
			@RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "dateRechercher", defaultValue = "") LocalDate mc,
			@RequestParam(name = "etatRechercher", defaultValue = "") String ec)

	{
		Page<Conge> conges = congeRepos.findByDateOrEtat(id, PageRequest.of(p, 4));

		model.addAttribute("conges", conges);
		model.addAttribute("motCle", mc);
		model.addAttribute("pageCourant", p);
		return "employeeDashboard";
	}

	@PostMapping(value = "/Valide")
	public String validateConge(Model model, @Valid Conge conge, @RequestParam(name = "id") Long id,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "formConge";
		if (id == null || conge == null || conge.getDateDebut() == null || conge.getDateFin() == null) {
			model.addAttribute("error", "paramètres invalides");
			return "NoValidate";
		}

		Employe employe = employeRepos.findById(id).orElse(null);
		long daysSinceEmployment = ChronoUnit.DAYS.between(employe.getDateEmbauchement(), LocalDate.now());
		if (daysSinceEmployment < 1) {
			model.addAttribute("error", "Vous n'etes pas employé depuis assez longtemps");
			return "NoValidate";
		}
		
		List<Conge> conges = congeRepos.findByEmploye(employe);
		int totalCongeDays = totalConge(conges);

		int requestedCongeDays = (int) ChronoUnit.DAYS.between(conge.getDateDebut(), conge.getDateFin());
		int totalRequestedDays = totalCongeDays + requestedCongeDays;

		if (totalRequestedDays < 30) {
			//admin lezem yamel el validation 
			conge.setEtat("SOLLICITE");
			model.addAttribute("conge", conge);
			congeRepos.save(conge);
			return "En Cours";
		} else {
			model.addAttribute("error", "Le nombre total de jours demandés dépasse la limite autorisée 30 jours ");
			return "NoValidate";
		}

	}
	@GetMapping(value="/NombreJourRestant")
	public String NombreJourRestant(Model model,@RequestParam(name = "id") Long id){
		Employe emp = employeRepos.getById(id);
		List<Conge> conges = congeRepos.findByEmploye(emp);
		int totalCongeDays = 30-totalConge(conges);
		model.addAttribute("nombrejourrestant", totalCongeDays);
		return "NombreJours";
		
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Long id, int page, String motCle) {
		Conge c=congeRepos.getById(id);
		if(c.getEtat().equals("SOLLICITE")) {
			congeRepos.deleteById(id);
		}
		return "redirect:index?page=" + page + "&motCle=" + motCle;
	}
	int totalConge(List<Conge> conges) {
		return conges.stream().mapToInt(c -> {
			LocalDate endDate = (c.getDateRepture() != null) ? c.getDateRepture() : c.getDateFin();
			return (int) ChronoUnit.DAYS.between(c.getDateDebut(), endDate);
		}).sum();
		
	}
}
