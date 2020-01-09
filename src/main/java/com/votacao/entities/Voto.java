package com.votacao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Voto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voto_seq" )
	@SequenceGenerator(sequenceName = "voto_id_seq", allocationSize = 1, name ="voto_seq")
	private Long idVoto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@Column
	private String opcao;

	@ManyToOne
	@JoinColumn(name="id_pauta", nullable=false)
	private Pauta pauta;	
	
	public Long getIdVoto() {
		return idVoto;
	}

	public void setIdVoto(Long idVoto) {
		this.idVoto = idVoto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}
}
