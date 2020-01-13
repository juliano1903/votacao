package com.votacao.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@Entity
public class Pauta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pauta_seq" )
	@SequenceGenerator(sequenceName = "pauta_id_seq", allocationSize = 1, name ="pauta_seq")
	private Long idPauta;
	
	@Column
	private String assunto;
	
	@Column
	private LocalDateTime dataTerminoSessao;
	
	@Column
	private boolean isSessaoEncerrada;
	
	@Transient
	private Long duracaoSessao;
	
	@OneToMany(mappedBy="pauta")
	private List<Voto> votos;

	public Long getIdPauta() {
		return idPauta;
	}

	public void setIdPauta(Long idPauta) {
		this.idPauta = idPauta;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public void setDataTerminoSessao(LocalDateTime dataTerminoSessao) {
		this.dataTerminoSessao = dataTerminoSessao;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}

	public Long getDuracaoSessao() {
		return duracaoSessao;
	}

	public void setDuracaoSessao(Long duracaoSessao) {
		this.duracaoSessao = duracaoSessao;
	}	

	public LocalDateTime getDataTerminoSessao() {
		return dataTerminoSessao;
	}

	public List<Voto> getVotos() {
		return votos;
	}

	public boolean isSessaoEncerrada() {
		return isSessaoEncerrada;
	}

	public void setSessaoEncerrada(boolean isSessaoEncerrada) {
		this.isSessaoEncerrada = isSessaoEncerrada;
	}
}
