package com.votacao.repositories;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.votacao.entities.Pauta;
import com.votacao.entities.Usuario;
import com.votacao.entities.Voto;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VotoRepositoryTest {
	
	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private PautaRepository pautaRepository;
	
	@Before
	public void setUp() {
		Pauta pauta = new Pauta();
		pauta.setAssunto("teste");
		this.pautaRepository.save(pauta);
		
		Voto voto = new Voto();
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);
		voto.setUsuario(usuario);
		voto.setPauta(pauta);
		
		this.votoRepository.save(voto);
	}
	
	@Test
	public void buscar_voto_por_idusuario_e_id_pauta() {
		Optional<Voto> voto = votoRepository.findByIdUsuarioAndIdPauta(1l, 1l);
		assertTrue(voto.isPresent());
	}
}
