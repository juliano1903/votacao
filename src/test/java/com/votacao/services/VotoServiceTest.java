package com.votacao.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.votacao.entities.Voto;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VotoServiceTest {
	
	@Autowired
	private VotoService votoService;
	
	@Test
	public void deve_lancar_excecao_quando_opcao_voto_for_diferente_de_sim_e_nao() {
		Voto voto = new Voto();
		voto.setOpcao("S");
		votoService.votar(voto);
	}
}
