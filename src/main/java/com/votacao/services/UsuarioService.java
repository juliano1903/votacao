package com.votacao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.votacao.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public boolean existeUsuario(Long idUsuario) {
		return usuarioRepository.findById(idUsuario).isPresent();
	}
}
