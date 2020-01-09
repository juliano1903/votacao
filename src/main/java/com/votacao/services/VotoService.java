package com.votacao.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.votacao.entities.Pauta;
import com.votacao.entities.Voto;
import com.votacao.repositories.PautaRepository;
import com.votacao.repositories.VotoRepository;

@Service
public class VotoService {

	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private PautaRepository pautaRepository;
	
	public Voto votar(Voto voto, BindingResult result) {
		
		Optional<Pauta> pautaExistente = pautaRepository.findById(voto.getPauta().getIdPauta());
		
		if(pautaExistente.isPresent()) {
			if(pautaExistente.get().getDataTerminoSessao() != null) {
				if (pautaExistente.get().getDataTerminoSessao().isBefore(LocalDateTime.now())) {
					result.addError(new ObjectError("pauta", "Não é possível votar nesta pauta. Sessão já encerrada!"));
					return null;
				}
			} else {
				result.addError(new ObjectError("pauta", "Não é possível votar nesta pauta. Sessão ainda não foi aberta!"));
			}
		} else {
			result.addError(new ObjectError("pauta", "Não é votar nesta pauta. Pauta inexistente!"));
			return null;
		}
		
		Optional<Voto> votoExistente = votoRepository.findByIdUsuarioAndIdPauta(voto.getUsuario().getIdUsuario(), voto.getPauta().getIdPauta());

		if (votoExistente.isPresent()) {
			result.addError(new ObjectError("pauta", "Não foi possível contabilizar o voto. Usuário já votou nesta pauta!"));
			return null;
		}
		return votoRepository.save(voto);
	}
}
