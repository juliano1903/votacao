package com.votacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.votacao.entities.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long>{}
