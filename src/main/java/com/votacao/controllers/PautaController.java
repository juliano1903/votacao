package com.votacao.controllers;

import java.net.URI;
import java.util.Optional;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.votacao.Response;
import com.votacao.dtos.PautaDto;
import com.votacao.entities.Pauta;
import com.votacao.services.PautaService;

@RestController
@RequestMapping("/api/pauta")
public class PautaController {
	
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
	
	@PatchMapping(value="/id/{id}")
	public ResponseEntity<Response<PautaDto>> iniciarSessao(@PathVariable("id") Long id, @RequestBody PautaDto pautaDTO, BindingResult result) {
		Response<PautaDto> response = new Response<>();
		pautaDTO.setId(id);
		pautaService.abrirSessao(convertDtoParaPauta(pautaDTO), result);
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping()
	public ResponseEntity<Response<PautaDto>> criarPauta(@RequestBody PautaDto pautaDTO, BindingResult result) {
		Pauta pauta = pautaService.criarPauta(convertDtoParaPauta(pautaDTO));
		return ResponseEntity.created(getUri(pauta.getIdPauta())).build();
	}
	
	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();
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
		pautaDto.setAssunto(pauta.getAssunto());
		pautaDto.setDuracaoSessao(pauta.getDuracaoSessao());
		pautaDto.setDataTerminoSessao(pauta.getDataTerminoSessao());
		return pautaDto;
	}
}
