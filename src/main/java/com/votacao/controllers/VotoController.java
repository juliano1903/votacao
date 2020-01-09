package com.votacao.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votacao.Response;
import com.votacao.dtos.VotoDto;
import com.votacao.entities.Pauta;
import com.votacao.entities.Usuario;
import com.votacao.entities.Voto;
import com.votacao.exceptions.BusinessException;
import com.votacao.services.VotoService;

@RestController
@RequestMapping(value="api/voto")
public class VotoController extends BaseController {

	@Autowired
	private VotoService votoService;
	
	@PostMapping()
	public ResponseEntity<Response<VotoDto>> votar(@Valid @RequestBody VotoDto votoDTO, BindingResult result) {
		Response<VotoDto> response = new Response<>();
		
		if(result.hasErrors()) {
			return geraBadRequestResponse(response, result);
		}
		
		try {
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
}
