package com.votacao.dtos;

import java.time.LocalDateTime;

import javax.persistence.Column;

public class PautaDto {

	private Long id;

	private Long duracaoSessao;
	
	private String assunto;
	
	private LocalDateTime dataTerminoSessao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDuracaoSessao() {
		return duracaoSessao;
	}

	public void setDuracaoSessao(Long duracaoSessao) {
		this.duracaoSessao = duracaoSessao;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public LocalDateTime getDataTerminoSessao() {
		return dataTerminoSessao;
	}

	public void setDataTerminoSessao(LocalDateTime dataTerminoSessao) {
		this.dataTerminoSessao = dataTerminoSessao;
	}
}
