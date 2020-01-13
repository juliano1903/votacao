package com.votacao.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.votacao.entities.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long>{
	
	@Query("select p from Pauta p "
			+ "join fetch p.votos v "
			+ "where p.dataTerminoSessao < :dataTerminoSessao "
			+ "and p.resultado is null")
	List<Pauta> findAllDataTerminoSessaoBeforeAndResultadoIsNull(@Param("dataTerminoSessao") LocalDateTime dataTerminoSessao);
}
