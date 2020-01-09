package com.votacao.controllers;

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
import com.votacao.entities.Pauta;
import com.votacao.services.PautaService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/pauta")
public class PautaController extends BaseController {
	
	@Autowired
	private PautaService pautaService;

	@GetMapping(value="/id/{id}")
	public ResponseEntity<Response<PautaDto>> buscarPorId(@PathVariable("id") Long idPauta) {
		Response<PautaDto> response = new Response<>();
		Optional<Pauta> pauta = pautaService.buscarPorId(idPauta);
		if (pauta.isPresent()) {
			response.setData(convertPautaParaDto(pauta.get()));
			return ResponseEntity.ok().body(response);
		}
		return ResponseEntity.notFound().build();
	}
	
	@SuppressWarnings("unchecked")
	@PatchMapping(value="/id/{id}")
	public ResponseEntity<Response<PautaDto>> iniciarSessao(@PathVariable("id") Long id, @RequestBody PautaDto pautaDTO, BindingResult result) {
		Response<PautaDto> response = new Response<>();
		pautaDTO.setId(id);
		pautaService.abrirSessao(convertDtoParaPauta(pautaDTO), result);
		if(result.hasErrors()) {
			return geraBadRequestResponse(response, result);
		}
		return ResponseEntity.noContent().build();
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
