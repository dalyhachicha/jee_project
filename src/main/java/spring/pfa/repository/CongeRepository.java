package spring.pfa.repository;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import spring.pfa.model.Conge;
import spring.pfa.model.Employe;

public interface CongeRepository extends  JpaRepository<Conge, Long>{
	List<Conge> findByEmploye(Employe employe);
	List<Conge> findByEtatLike(String e);
	List<Conge> findByDateDebutAfter(LocalDate d);
	@Query("SELECT c FROM Conge c WHERE c.employe.id = :id")
    Page<Conge> findByDateOrEtat(@Param("id") Long id,Pageable pageable);
}
