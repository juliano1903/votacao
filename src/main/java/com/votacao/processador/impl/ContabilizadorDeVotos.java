package com.votacao.processador.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.votacao.entities.Voto;

public class ContabilizadorDeVotos {

	private Map<String, List<String>> resultado;
	
	public void contabilizar(List<Voto> votos) {
		
		resultado = new HashMap<>();
		resultado.putIfAbsent("SIM", new ArrayList<String>());
		resultado.putIfAbsent("NAO", new ArrayList<String>());
		
		votos.forEach(s -> 
		resultado.computeIfAbsent(s.getOpcao(), k -> new ArrayList<>()) // or LinkedHashSet
		        .add(s.getOpcao()));
	}
	
	public String getVencedor() {
		List<String> listSim = resultado.get("SIM");
		List<String> listNao = resultado.get("NAO");
		if(listSim.size() > listNao.size()) {
			return "SIM";
		} else if(listNao.size() > listSim.size()) {
			return "NAO";
		}
		return "EMPATE";
	}
}
