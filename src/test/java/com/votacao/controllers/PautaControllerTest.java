package com.votacao.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votacao.dtos.VotoDto;
import com.votacao.entities.Pauta;
import com.votacao.entities.Usuario;
import com.votacao.entities.Voto;
import com.votacao.exceptions.BusinessException;
import com.votacao.services.PautaService;
import com.votacao.services.VotoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PautaControllerTest {
	
	 private static final String BASE_URI_PAUTAS = "/api/v1/pauta";

	 private static final String URI_PAUTAS_1 = "/api/v1/pauta/1";
	 
	 private static final String URI_VOTOS = "/api/v1/pauta/1/votar";
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PautaService pautaService;
	
	@MockBean
	private VotoService votoService;
	
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		this.objectMapper = new ObjectMapper();
	}
	
	@Test
	public void deve_retornar_pauta_quando_buscar_por_id_valido() throws Exception {
		Pauta pauta = new Pauta();
		pauta.setAssunto("TESTE");
		pauta.setIdPauta(1l);
		BDDMockito.given(this.pautaService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(pauta));
		
		mockMvc.perform(MockMvcRequestBuilders.get(URI_PAUTAS_1))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.assunto").value("TESTE"))
		.andExpect(jsonPath("$.data.id").value(1));
	}
	
	@Test
	public void deve_retornar_not_found_quando_nao_encrontrar_pauta_pelo_id() throws Exception {
		Pauta pauta = new Pauta();
		pauta.setAssunto("TESTE");
		pauta.setIdPauta(1l);
		BDDMockito.given(this.pautaService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.get(URI_PAUTAS_1))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deve_retornar_no_content_quando_abrir_sessao() throws Exception {
		HashMap<String, Object> propriedades = new HashMap<>();
		propriedades.put("duracaoSessao", "");
		
		BDDMockito.given(this.pautaService.abrirSessao(Mockito.any(Pauta.class))).willReturn(new Pauta());
		
		mockMvc.perform(MockMvcRequestBuilders.patch(URI_PAUTAS_1).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(propriedades)))
		.andExpect(status().isNoContent());
	}
	
	@Test
	public void deve_retornar_bad_request_quando_lancar_excecao() throws Exception {
		HashMap<String, Object> propriedades = new HashMap<>();
		propriedades.put("duracaoSessao", "");
		
		BDDMockito.given(this.pautaService.abrirSessao(Mockito.any(Pauta.class))).willThrow(BusinessException.class);
		
		mockMvc.perform(MockMvcRequestBuilders.patch(URI_PAUTAS_1).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(propriedades)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deve_retornar_bad_request_quando_criar_pauta_sem_assunto() throws Exception {
		HashMap<String, Object> propriedades = new HashMap<>();
		propriedades.put("assunto", "");
		
		mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI_PAUTAS).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(propriedades)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.errors").value("Assunto não pode ser vazio"));
	}
	
	@Test
	public void deve_retornar_created_quando_pauta_for_criada() throws Exception {
		HashMap<String, Object> propriedades = new HashMap<>();
		propriedades.put("assunto", "TESTE");
		
		Pauta pauta = new Pauta();
		pauta.setIdPauta(1l);
		
		BDDMockito.given(this.pautaService.criarPauta(Mockito.any(Pauta.class))).willReturn(pauta);
		
		mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI_PAUTAS).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(propriedades)))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void deve_retornar_bad_request_quando_dto_nao_estiver_completo() throws Exception {
		VotoDto votoDto = new VotoDto();
		
		mockMvc.perform(MockMvcRequestBuilders.post(URI_VOTOS).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(votoDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deve_retornar_created_quando_quando_voto_for_computado() throws Exception {
		VotoDto votoDto = new VotoDto();
		votoDto.setOpcao("OPCAO");
		votoDto.setIdPauta(1l);
		votoDto.setIdUsuario(1l);
		
		Pauta pauta = new Pauta();
		pauta.setAssunto("teste");
		pauta.setIdPauta(1l);
		
		Voto voto = new Voto();
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1l);
		voto.setUsuario(usuario);
		voto.setPauta(pauta);
		
		BDDMockito.given(this.votoService.votar(Mockito.any(Voto.class))).willReturn(voto);

		mockMvc.perform(MockMvcRequestBuilders.post(URI_VOTOS).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(votoDto)))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void deve_retornar_bad_request_quando_lancar_excecao_ao_votar() throws Exception {
		VotoDto votoDto = new VotoDto();
		votoDto.setOpcao("OPCAO");
		votoDto.setIdPauta(1l);
		votoDto.setIdUsuario(1l);
		
		BDDMockito.given(this.votoService.votar(Mockito.any(Voto.class))).willThrow(BusinessException.class);

		mockMvc.perform(MockMvcRequestBuilders.post(URI_VOTOS).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(votoDto)))
		.andExpect(status().isBadRequest());
	}
}
