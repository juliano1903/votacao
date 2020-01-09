package com.votacao.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.votacao.entities.Pauta;
import com.votacao.repositories.PautaRepository;

@Service
public class PautaService {
	
	@Autowired
	private PautaRepository pautaRepository;

	public Pauta criarPauta(Pauta pauta) {
		return pautaRepository.save(pauta);
	}
	
	public Pauta abrirSessao(Pauta pauta, BindingResult result) {
		
		Optional<Pauta> pautaExistente = pautaRepository.findById(pauta.getIdPauta());
		
		if(pautaExistente.isPresent()) {
			if(pautaExistente.get().getDataTerminoSessao() != null) {
				if (pautaExistente.get().getDataTerminoSessao().isBefore(LocalDateTime.now())) {
					result.addError(new ObjectError("pauta", "Não é possível abrir esta sessão. Sessao já encerrada!"));
				} else {
					result.addError(new ObjectError("pauta", "Não é possível abrir esta sessão. Sessao já aberta!"));
				}
				return null;
			}
		} else {
			result.addError(new ObjectError("pauta", "Não é possível abrir esta sessão. Pauta inexistente!"));
			return null;
		}
		
		if(pauta.getDuracaoSessao() == null) {
			pauta.setDuracaoSessao(1l);
		}
		LocalDateTime dataTerminoSessao = LocalDateTime.now().plusMinutes(pauta.getDuracaoSessao());
		pauta.setDataTerminoSessao(dataTerminoSessao);
		return pautaRepository.save(pauta);
	}
	
	public Optional<Pauta> buscarPorId(Long idPauta) {
		return pautaRepository.findById(idPauta);
	}
}
