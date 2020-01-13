package com.votacao.processador.impl;
import java.io.Console;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.votacao.entities.Pauta;
import com.votacao.processador.Processador;
import com.votacao.repositories.PautaRepository;

@Component
public class ProcessadorDeResultado implements Processador {

	@Autowired
	private PautaRepository pautaRepository;
	
	@Override
	@Scheduled(fixedRate = 30000)
	public void processar() {
		List<Pauta> pautasEncerradas = pautaRepository.findAllDataTerminoSessaoBeforeAndResultadoIsNull(LocalDateTime.now());
		ContabilizadorDeVotos contabilizador = new ContabilizadorDeVotos();		
		for (Pauta pauta : pautasEncerradas) {
			contabilizador.contabilizar(pauta.getVotos());
			pauta.setResultado(contabilizador.getVencedor());
			pauta.setSessaoEncerrada(true);
			pautaRepository.save(pauta);
		}
	}
}
