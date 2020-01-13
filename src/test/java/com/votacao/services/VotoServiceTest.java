package com.votacao.services;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.votacao.entities.Pauta;
import com.votacao.entities.Usuario;
import com.votacao.entities.Voto;
import com.votacao.exceptions.BusinessException;
import com.votacao.repositories.PautaRepository;
import com.votacao.repositories.UsuarioRepository;
import com.votacao.repositories.VotoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VotoServiceTest {
	
	@Autowired
	private VotoService votoService;
	
	@MockBean
	private PautaRepository pautaRepository;
	
	@MockBean
	private VotoRepository votoRepository;
	
	@MockBean
	private UsuarioService usuarioService;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void deve_lancar_excecao_quando_opcao_voto_for_diferente_de_sim_e_nao() {
	    expectedEx.expect(BusinessException.class);
	    expectedEx.expectMessage("Valores permitidos para o voto são SIM ou NAO");
		
	    Voto voto = new Voto();
		voto.setOpcao("S");
		votoService.votar(voto);
	}
	
	@Test
	public void deve_lancar_excecao_quando_a_pauta_for_inexistente() {
	    expectedEx.expect(BusinessException.class);
	    expectedEx.expectMessage("Não é possível votar nesta pauta. Pauta inexistente");
		
		Pauta pauta = new Pauta();
		pauta.setIdPauta(1l);
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);
		
		Voto voto = new Voto();
		voto.setOpcao("SIM");
		
		voto.setPauta(pauta);
		voto.setUsuario(usuario);
		given(this.usuarioService.existeUsuario(any(Long.class))).willReturn((true));
		
		votoService.votar(voto);
	}
	
	@Test
	public void deve_lancar_excecao_quando_a_pauta_ja_estiver_encerrada() {
		expectedEx.expect(BusinessException.class);
		expectedEx.expectMessage("Não é possível votar nesta pauta. Sessão já encerrada");

		Pauta pauta = new Pauta();
		pauta.setDataTerminoSessao(now().minusMonths(1));
		pauta.setIdPauta(1l);
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);

		Voto voto = new Voto();
		voto.setOpcao("SIM");
		pauta.setIdPauta(1l);
		voto.setPauta(pauta);
		voto.setUsuario(usuario);
	    
		given(this.usuarioService.existeUsuario(any(Long.class))).willReturn((true));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));
	    
		votoService.votar(voto);
	}
	
	@Test
	public void deve_lancar_excecao_quando_a_pauta_ainda_nao_estiver_aberta() {
		expectedEx.expect(BusinessException.class);
		expectedEx.expectMessage("Não é possível votar nesta pauta. Sessão ainda não foi aberta");

		Pauta pauta = new Pauta();
		pauta.setIdPauta(1l);
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);
		
		Voto voto = new Voto();
		voto.setOpcao("NAO");
		voto.setPauta(pauta);
		voto.setUsuario(usuario);
	    
		given(this.usuarioService.existeUsuario(any(Long.class))).willReturn((true));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));

	    
		votoService.votar(voto);
	}
	
	@Test
	public void deve_lancar_excecao_quando_a_usuario_nao_existir() {
		expectedEx.expect(BusinessException.class);
		expectedEx.expectMessage("Não foi possível contabilizar o voto. Usuário inexistente");

		Pauta pauta = new Pauta();
		pauta.setIdPauta(1l);
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);
		
		Voto voto = new Voto();
		voto.setOpcao("NAO");
		voto.setPauta(pauta);
		voto.setUsuario(usuario);
	    
		given(this.usuarioService.existeUsuario(any(Long.class))).willReturn((false));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));

	    
		votoService.votar(voto);
	}
	
	@Test
	public void deve_lancar_excecao_quando_usuario_ja_votou_nesta_pauta() {
		expectedEx.expect(BusinessException.class);
		expectedEx.expectMessage("Não foi possível contabilizar o voto. Usuário já votou nesta pauta");

		Pauta pauta = new Pauta();
		pauta.setDataTerminoSessao(now().plusMonths(1));
		pauta.setIdPauta(1l);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);

		Voto voto = new Voto();
		voto.setOpcao("NAO");
		voto.setPauta(pauta);
		voto.setUsuario(usuario);
	    
		given(this.usuarioService.existeUsuario(any(Long.class))).willReturn((true));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));
	    given(this.votoRepository.findByIdUsuarioAndIdPauta(any(Long.class), any(Long.class))).willReturn((of(voto)));
	    
		votoService.votar(voto);
	}
	
	@Test
	public void deve_salvar_voto_quando_passar_por_todas_as_validacoes() {
		Pauta pauta = new Pauta();
		pauta.setDataTerminoSessao(now().plusMonths(1));
		pauta.setIdPauta(1l);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);
		Voto voto = new Voto();
		voto.setOpcao("NAO");
		pauta.setIdPauta(1l);
		voto.setPauta(pauta);
		voto.setUsuario(usuario);
	    
		given(this.usuarioService.existeUsuario(any(Long.class))).willReturn((true));
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((of(pauta)));
	    given(this.votoRepository.findByIdUsuarioAndIdPauta(any(Long.class), any(Long.class))).willReturn((empty()));

		votoService.votar(voto);

		verify(this.votoRepository, atLeastOnce()).save(any(Voto.class));
	}
}
