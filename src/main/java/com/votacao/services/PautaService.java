package com.votacao.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.votacao.entities.Pauta;
import com.votacao.exceptions.BusinessException;
import com.votacao.repositories.PautaRepository;

@Service
public class PautaService {
	
	@Autowired
	private PautaRepository pautaRepository;

	public Pauta criarPauta(Pauta pauta) {
		return pautaRepository.save(pauta);
	}
	
	public Pauta abrirSessao(Pauta pauta) {
		
		Optional<Pauta> pautaExistente = pautaRepository.findById(pauta.getIdPauta());
		
		if(pautaExistente.isPresent()) {
			if(pautaExistente.get().getDataTerminoSessao() != null) {
				if (pautaExistente.get().getDataTerminoSessao().isBefore(LocalDateTime.now())) {
					throw new BusinessException("Não é possível abrir esta sessão. Sessao já encerrada");
				} else {
					throw new BusinessException("Não é possível abrir esta sessão. Sessao já aberta");
				}
			}
		} else {
			throw new BusinessException("Não é possível abrir esta sessão. Pauta inexistente");
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

	public List<Pauta> findAll() {
		return pautaRepository.findAll();
	}
}
