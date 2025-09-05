package com.sicredi.pautachallenge.service;

import java.util.Optional;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.sicredi.pautachallenge.domain.dto.VoteDTO;
import com.sicredi.pautachallenge.domain.model.Section;
import com.sicredi.pautachallenge.domain.model.VoteStatus;
import com.sicredi.pautachallenge.domain.model.Votes;
import com.sicredi.pautachallenge.repository.SectionRepository;
import com.sicredi.pautachallenge.repository.VotesRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotesService {
    private final VotesRepository repository;
    private final SectionRepository sectionRepository;

    public Votes createVote(VoteDTO voteDTO) {
        log.info("Processando criação de voto. Usuário: {}, Seção: {}, Voto: {}", 
                voteDTO.userId(), voteDTO.sectionId(), voteDTO.vote());
        
        // Validate section exists and is not expired
        validateSection(voteDTO.sectionId());
        
        if (!isValidCPF(voteDTO.userId())) {
            return createInvalidVote(voteDTO);
        }

        log.debug("CPF validado com sucesso para usuário: {}", voteDTO.userId());
        
        Votes votes = createVoteFromDTO(voteDTO);
        Votes createdVote = processVoteCreation(votes);
        
        return processVoteResult(createdVote, voteDTO);
    }

    private void validateSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId).orElse(null);
        if (section == null) {
            log.warn("Tentativa de voto em seção inexistente: {}", sectionId);
            throw new IllegalArgumentException("Seção não encontrada");
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = section.getStart_at().plusMinutes(section.getExpiration());
        
        if (now.isAfter(expirationTime)) {
            log.warn("Tentativa de voto em seção expirada: {} (expirou em: {})", sectionId, expirationTime);
            throw new IllegalArgumentException("Seção expirada");
        }
        
        log.debug("Seção validada com sucesso: {} (expira em: {})", sectionId, expirationTime);
    }

    private boolean isValidCPF(Long userId) {
        Random random = new Random();
        return random.nextDouble() > 0.3;
    }

    private Votes createInvalidVote(VoteDTO voteDTO) {
        log.warn("CPF inválido detectado para usuário: {}", voteDTO.userId());
        Votes invalidVote = new Votes();
        invalidVote.setUserId(voteDTO.userId());
        invalidVote.setSectionId(voteDTO.sectionId());
        invalidVote.setVote(voteDTO.vote());
        invalidVote.setStatus(VoteStatus.UNABLE_TO_VOTE);
        return invalidVote;
    }

    private Votes createVoteFromDTO(VoteDTO voteDTO) {
        Votes votes = new Votes();
        votes.setUserId(voteDTO.userId());
        votes.setSectionId(voteDTO.sectionId());
        votes.setVote(voteDTO.vote());
        votes.setStatus(VoteStatus.ABLE_TO_VOTE);
        return votes;
    }

    private Votes processVoteCreation(Votes votes) {
        log.debug("Verificando se já existe voto para usuário: {} na seção: {}", votes.getUserId(), votes.getSectionId());
        
        if (isVoteAlreadyExists(votes)) {
            return null;
        }

        return saveVote(votes);
    }

    private boolean isVoteAlreadyExists(Votes votes) {
        Optional<Votes> existingVote = repository.findByUserIdAndSectionId(votes.getUserId(), votes.getSectionId());
        if (existingVote.isPresent()) {
            log.warn("Voto já existe para usuário: {} na seção: {}", votes.getUserId(), votes.getSectionId());
            return true;
        }
        return false;
    }

    private Votes saveVote(Votes votes) {
        log.debug("Salvando novo voto para usuário: {} na seção: {}", votes.getUserId(), votes.getSectionId());
        Votes savedVote = repository.save(votes);
        log.debug("Voto salvo com sucesso. ID: {}, Usuário: {}, Seção: {}", 
                savedVote.getId(), savedVote.getUserId(), savedVote.getSectionId());
        return savedVote;
    }

    private Votes processVoteResult(Votes createdVote, VoteDTO voteDTO) {
        if (createdVote != null) {
            log.info("Voto criado com sucesso. ID: {}, Usuário: {}, Seção: {}", 
                    createdVote.getId(), createdVote.getUserId(), createdVote.getSectionId());
            return createdVote;
        } else {
            log.warn("Tentativa de voto duplicado. Usuário: {}, Seção: {}", voteDTO.userId(), voteDTO.sectionId());
            throw new IllegalArgumentException("Esse usuário já votou nesta seção.");
        }
    }
}
