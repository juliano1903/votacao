package com.votacao.services;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.votacao.entities.Pauta;
import com.votacao.entities.Voto;
import com.votacao.exceptions.BusinessException;
import com.votacao.repositories.PautaRepository;
import com.votacao.repositories.VotoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PautaServiceTest {

	@Autowired
	private PautaService pautaService;
	
	@MockBean
	private PautaRepository pautaRepository;
	
	@MockBean
	private VotoRepository votoRepository;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void deve_lancar_excecao_quando_tentar_abrir_sessao_de_pauta_inexistente() {
	    expectedEx.expect(BusinessException.class);
	    expectedEx.expectMessage("Não é possível abrir esta sessão. Pauta inexistente");

	    given(this.pautaRepository.findById(any(Long.class))).willReturn((empty()));
	    
	    pautaService.abrirSessao(new Pauta());

	}
	
	@Test
	public void deve_lancar_excecao_quando_tentar_abrir_sessao_ja_aberta() {
	    expectedEx.expect(BusinessException.class);
	    expectedEx.expectMessage("Não é possível abrir esta sessão. Sessao já aberta");

	    Pauta pauta = new Pauta();
	    pauta.setIdPauta(1l);
	    pauta.setDataTerminoSessao(LocalDateTime.now().plusMonths(1));
	    
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((Optional.of(pauta)));
	    
	    pautaService.abrirSessao(pauta);

	}
	
	@Test
	public void deve_lancar_excecao_quando_tentar_abrir_sessao_ja_encerrada() {
	    expectedEx.expect(BusinessException.class);
	    expectedEx.expectMessage("Não é possível abrir esta sessão. Sessao já encerrada");

	    Pauta pauta = new Pauta();
	    pauta.setIdPauta(1l);
	    pauta.setDataTerminoSessao(LocalDateTime.now().minusMonths(1));
	    
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((Optional.of(pauta)));
	    
	    pautaService.abrirSessao(pauta);

	}
	
	@Test
	public void deve_abrir_sessao_com_valor_definido_quando_duracao_nao_for_nulo() {
	    Pauta pauta = Mockito.spy(new Pauta());
	    pauta.setIdPauta(1l);
	    pauta.setDuracaoSessao(100l);
	    
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((Optional.of(pauta)));

	    pautaService.abrirSessao(pauta);
	    
		verify(pauta, times(0)).setDuracaoSessao(1l);
		verify(pautaRepository, times(1)).save(any(Pauta.class));
	}
	
	@Test
	public void deve_abrir_sessao_com_valor_default_quando_duracao_for_nulo() {
	    Pauta pauta = Mockito.spy(new Pauta());
	    pauta.setIdPauta(1l);
	    
	    given(this.pautaRepository.findById(any(Long.class))).willReturn((Optional.of(pauta)));

	    pautaService.abrirSessao(pauta);
	    
		verify(pauta, times(1)).setDuracaoSessao(1l);
		verify(pautaRepository, times(1)).save(any(Pauta.class));
	}
	
	@Test
	public void deve_buscar_pauta_por_id() {
		pautaService.buscarPorId(1l);
		verify(this.pautaRepository, times(1)).findById(any(Long.class));
	}
	
	@Test
	public void deve_buscar_pauta() {
		pautaService.criarPauta(new Pauta());
		verify(this.pautaRepository, times(1)).save(any(Pauta.class));
	}
}
