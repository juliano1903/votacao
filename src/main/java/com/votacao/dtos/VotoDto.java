package com.votacao.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VotoDto {

	private Long id;
	
	private Long idPauta;
	
	private String opcao;
	
	private Long idUsuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "Códido do usuário deve ser informado")
	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	@NotNull(message = "Códido do usuário deve ser informado")
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	@NotNull(message = "Códido da pauta deve ser informado")
	public Long getIdPauta() {
		return idPauta;
	}

	public void setIdPauta(Long idPauta) {
		this.idPauta = idPauta;
	}
}
