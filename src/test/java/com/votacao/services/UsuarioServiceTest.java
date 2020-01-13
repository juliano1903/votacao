package com.votacao.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	public void deve_retornar_false_quando_usuario_nao_existir() {
	    assertFalse(this.usuarioService.existeUsuario(5l));
	}
	
	@Test
	public void deve_retornar_true_quando_usuario_nao_existir() {
	    assertTrue(this.usuarioService.existeUsuario(1l));
	}
}
