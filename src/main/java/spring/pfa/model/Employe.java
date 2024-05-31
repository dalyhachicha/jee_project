package spring.pfa.model;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.validation.constraints.Size;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Employe extends Utilisateur {
	@OneToMany(mappedBy = "employe")
	private Collection<Conge> conge = new ArrayList<Conge>();




	@Override
	public String toString() {
		return "Employe [conge=" + conge + ", id=" + id + ", email=" + email + ", nom=" + nom + ", prenom=" + prenom
				+ ", dateEmbauchement=" + dateEmbauchement + ", password=" + password + "]";
	}



	public Employe() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Employe(Long id, String email, String nom, String prenom, LocalDate dateEmbauchement,
			@Size(min = 6, max = 10, message = "Le mot de passe doit contenir entre 6 et 10 caractères.") String password) {
		super(id, email, nom, prenom, dateEmbauchement, password);
		// TODO Auto-generated constructor stub
	}



	public Collection<Conge> getConge() {
		return conge;
	}

	public void setConge(Collection<Conge> conge) {
		this.conge = conge;
	}

}
