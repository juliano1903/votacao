package com.votacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votacao.entities.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{}
