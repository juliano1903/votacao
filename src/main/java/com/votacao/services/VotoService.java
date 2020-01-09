package com.votacao.services;

import static com.votacao.OpcaoVotoEnum.NAO;
import static com.votacao.OpcaoVotoEnum.SIM;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.votacao.OpcaoVotoEnum;
import com.votacao.entities.Pauta;
import com.votacao.entities.Voto;
import com.votacao.exceptions.BusinessException;
import com.votacao.repositories.PautaRepository;
import com.votacao.repositories.VotoRepository;

@Service
public class VotoService {

	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private PautaRepository pautaRepository;
	
	public Voto votar(Voto voto) {
		if (!SIM.name().equals(voto.getOpcao()) && !NAO.name().equals(voto.getOpcao())) {
			throw new BusinessException("Valores permitidos para o voto são SIM ou NAO");
		}
		
		Optional<Pauta> pautaExistente = pautaRepository.findById(voto.getPauta().getIdPauta());
		
		if(pautaExistente.isPresent()) {
			if(pautaExistente.get().getDataTerminoSessao() != null) {
				if (pautaExistente.get().getDataTerminoSessao().isBefore(LocalDateTime.now())) {
					throw new BusinessException("Não é possível votar nesta pauta. Sessão já encerrada");
				}
			} else {
				throw new BusinessException("Não é possível votar nesta pauta. Sessão ainda não foi aberta");
			}
		} else {
			throw new BusinessException("Não é possível votar nesta pauta. Pauta inexistente");
		}
		
		Optional<Voto> votoExistente = votoRepository.findByIdUsuarioAndIdPauta(voto.getUsuario().getIdUsuario(), voto.getPauta().getIdPauta());

		if (votoExistente.isPresent()) {
			throw new BusinessException("Não foi possível contabilizar o voto. Usuário já votou nesta pauta");
		}
		
		return votoRepository.save(voto);
	}
}
