package com.votacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.votacao.entities.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long>{

	@Query("select p from Pauta p "
			+ " join fetch p.votos "
			+ " where p.idPauta = : dPauta ")
	Pauta findByIdPauta(Long idPauta);
}
