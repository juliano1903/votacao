package com.votacao.dtos;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PautaDto {

	private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private Long duracaoSessao;
	
	private String assunto;
	
	private LocalDateTime dataTerminoSessao;
	
	private String resultado;
	
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

	@NotEmpty(message = "Assunto não pode ser vazio")
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

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
}
