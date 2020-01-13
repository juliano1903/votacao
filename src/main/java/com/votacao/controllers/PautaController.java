package com.votacao.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votacao.Response;
import com.votacao.dtos.PautaDto;
import com.votacao.dtos.VotoDto;
import com.votacao.entities.Pauta;
import com.votacao.entities.Usuario;
import com.votacao.entities.Voto;
import com.votacao.exceptions.BusinessException;
import com.votacao.services.PautaService;
import com.votacao.services.VotoService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/v1/pauta")
public class PautaController extends BaseController {
	
	@Autowired
	private PautaService pautaService;
	
	@Autowired
	private VotoService votoService;

	@GetMapping(value="/{id}")
	public ResponseEntity<Response<PautaDto>> buscarPorId(@PathVariable("id") Long idPauta) {
		Response<PautaDto> response = new Response<>();
		Optional<Pauta> pauta = pautaService.buscarPorId(idPauta);
		if (pauta.isPresent()) {
			response.setData(convertPautaParaDto(pauta.get()));
			return ResponseEntity.ok().body(response);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping()
	public ResponseEntity<Response<List<PautaDto>>> buscarTodasPautas() {
		Response<List<PautaDto>> response = new Response<>();
		List<Pauta> pautas = pautaService.findAll();
		if (!pautas.isEmpty()) {
			List<PautaDto> pautasDto = new ArrayList<>();
			for (Pauta pauta: pautas) {
				pautasDto.add(convertPautaParaDto(pauta));
			}
			
			response.setData(pautasDto);				
			return ResponseEntity.ok().body(response);
		}
		return ResponseEntity.notFound().build();
	}
	
	@SuppressWarnings("unchecked")
	@PatchMapping(value="/{id}")
	public ResponseEntity<Response<PautaDto>> iniciarSessao(@PathVariable("id") Long id, @RequestBody PautaDto pautaDTO, BindingResult result) {
		Response<PautaDto> response = new Response<>();

		try {
			pautaDTO.setId(id);
			pautaService.abrirSessao(convertDtoParaPauta(pautaDTO));
			return ResponseEntity.noContent().build();			
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return geraBadRequestResponse(response, result);
		}
	}
	
	@PostMapping()
	public ResponseEntity<Response<PautaDto>> criarPauta(@Valid @RequestBody PautaDto pautaDTO, BindingResult result) {
		Response<PautaDto> response = new Response<>();
		if(result.hasErrors()) {
			return geraBadRequestResponse(response, result);
		}
		Pauta pauta = pautaService.criarPauta(convertDtoParaPauta(pautaDTO));
		return ResponseEntity.created(getUri(pauta.getIdPauta())).build();
	}
	
	@PostMapping("/{id}/votar")
	public ResponseEntity<Response<VotoDto>> votar(@PathVariable("id") Long idPauta, @Valid @RequestBody VotoDto votoDTO, BindingResult result) {
		Response<VotoDto> response = new Response<>();
		
		if(result.hasErrors()) {
			return geraBadRequestResponse(response, result);
		}
		
		try {
			votoDTO.setIdPauta(idPauta);
			Voto voto = votoService.votar(convertDtoParaVoto(votoDTO));
			response.setData(converterVotoParaVotoDto(voto));
			return ResponseEntity.created(getUri(voto.getIdVoto())).build();
		} catch (BusinessException e) {
			response.getErrors().add(e.getMessage());
			return geraBadRequestResponse(response, result);
		}
	}

	private VotoDto converterVotoParaVotoDto(Voto voto) {
		VotoDto votoDto = new VotoDto();
		votoDto.setIdPauta(voto.getPauta().getIdPauta());
		votoDto.setOpcao(voto.getOpcao());
		votoDto.setIdUsuario(voto.getUsuario().getIdUsuario());
		return votoDto;
	}

	private Voto convertDtoParaVoto(VotoDto votoDTO) {
		Voto voto = new Voto();
		voto.setOpcao(votoDTO.getOpcao());
		Pauta pauta = new Pauta();
		pauta.setIdPauta(votoDTO.getIdPauta());
		voto.setPauta(pauta);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(votoDTO.getIdUsuario());
		voto.setUsuario(usuario);
		return voto;
	}
 	
	private Pauta convertDtoParaPauta(PautaDto pautaDTO) {
		Pauta pauta = new Pauta();
		pauta.setAssunto(pautaDTO.getAssunto());
		pauta.setIdPauta(pautaDTO.getId());
		pauta.setDuracaoSessao(pautaDTO.getDuracaoSessao());
		pauta.setDataTerminoSessao(pautaDTO.getDataTerminoSessao());
		return pauta;
	}
	
	private PautaDto convertPautaParaDto(Pauta pauta) {
		PautaDto pautaDto = new PautaDto();
		pautaDto.setId(pauta.getIdPauta());
		pautaDto.setAssunto(pauta.getAssunto());
		pautaDto.setDataTerminoSessao(pauta.getDataTerminoSessao());
		return pautaDto;
	}
}
