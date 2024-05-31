package spring.pfa.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import spring.pfa.model.Conge;
import spring.pfa.model.Employe;
import spring.pfa.repository.CongeRepository;
import spring.pfa.repository.EmployeRepository;

@Controller
@RequestMapping(value = "/admin")
public class AdministrateurController {
	private final CongeRepository congeRepos;
	private final EmployeRepository employeRepos;

	@Autowired
	public AdministrateurController(CongeRepository congeRepos, EmployeRepository employeRepos) {
		this.congeRepos = congeRepos;
		this.employeRepos = employeRepos;
	}
	@RequestMapping(value = "/index")
	public String index(Model model,
			@RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "nom", defaultValue = "") String nom,
			@RequestParam(name = "dateRechercher", defaultValue = "") LocalDate dr,
			@RequestParam(name = "etatRechercher", defaultValue = "") String ec)

	{
		Page<Employe> employes = employeRepos.findByEmployeOrDateOrEtat(nom, PageRequest.of(p, 4));
		
		//tous les employes 
		model.addAttribute("employes", employes);
		model.addAttribute("pageCourant", p);
		model.addAttribute("dateRecherche", dr);
		model.addAttribute("etatRechercher", ec);
		model.addAttribute("nom", nom);
		return "adminDashboard";
	}
//	@RequestMapping(value = "/CongeEmploye")
//	public String CongeEmploye(Model model, @RequestParam(name = "id") Long id,
//			@RequestParam(name = "page", defaultValue = "0") int p,
//			@RequestParam(name = "dateRechercher", required = false) LocalDate mc,
//			@RequestParam(name = "etatRechercher", defaultValue = "") String ec)
//
//	{
//		 if (mc == null) {
//	            mc = LocalDate.now();
//        }
//		Page<Conge> CongeHistorique = congeRepos.findByDateOrEtat(id, mc, ec, PageRequest.of(p, 4));
//		model.addAttribute("congeHisto", CongeHistorique);
//		model.addAttribute("motCle", mc);
//		model.addAttribute("pageCourant", p);
//		return "Profil";
//	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, @RequestParam(name = "id") Long id)

	{
		// récupérer l'objet ayant l'id spécifié
		Conge c = congeRepos.findById(id).orElse(null);
		
		model.addAttribute("conge", c);
		// rediriger l'affichage vers la vue "editProduit"
		return "Validation";
	}

	@RequestMapping(value = "/updateValidation", method = RequestMethod.POST)
	public String update(Model model, @Valid Conge c) {

		congeRepos.save(c);
		return "Profil";

	}

}
