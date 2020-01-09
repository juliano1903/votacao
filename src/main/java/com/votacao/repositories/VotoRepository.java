package com.votacao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.votacao.entities.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {
	
	Voto findByIdVoto(Long idVoto);
	
	@Query("select v from Voto v "
			+ " where v.usuario.idUsuario = :idUsuario "
			+ " and v.pauta.idPauta = :idPauta")
	Optional<Voto> findByIdUsuarioAndIdPauta(@Param("idUsuario") Long idUsuario, @Param("idPauta") Long idPauta);

}
