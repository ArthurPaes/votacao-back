package com.sicredi.pautachallenge.service;

import java.time.LocalDateTime;
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
        if (!isValidSection(voteDTO.sectionId())) {
            throw new IllegalArgumentException("Seção não encontrada ou expirada.");
        }
        
        if (!isValidCPF(voteDTO.userId())) {
            return saveInvalidVote(voteDTO);
        }

        log.debug("CPF validado com sucesso para usuário: {}", voteDTO.userId());
        
        Votes votes = createVoteFromDTO(voteDTO);
        Votes createdVote = processVoteCreation(votes);
        
        return processVoteResult(createdVote, voteDTO);
    }

    private boolean isValidSection(Long sectionId) {
        Optional<Section> sectionOpt = sectionRepository.findById(sectionId);
        if (sectionOpt.isEmpty()) {
            log.warn("Seção não encontrada: {}", sectionId);
            return false;
        }
        
        Section section = sectionOpt.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = section.getStart_at().plusMinutes(section.getExpiration());
        
        if (now.isAfter(expirationTime)) {
            log.warn("Seção expirada: {} (expirou em: {})", sectionId, expirationTime);
            return false;
        }
        
        return true;
    }

    private boolean isValidCPF(Long userId) {
        Random random = new Random();
        return random.nextDouble() > 0.3;
    }

    private Votes saveInvalidVote(VoteDTO voteDTO) {
        log.warn("CPF inválido detectado para usuário: {}", voteDTO.userId());
        Votes invalidVote = new Votes();
        invalidVote.setUserId(voteDTO.userId());
        invalidVote.setSectionId(voteDTO.sectionId());
        invalidVote.setVote(voteDTO.vote());
        invalidVote.setStatus(VoteStatus.UNABLE_TO_VOTE);
        
        // Save the invalid vote to get an ID
        Votes savedInvalidVote = repository.save(invalidVote);
        log.debug("Voto inválido salvo com sucesso. ID: {}, Usuário: {}, Seção: {}", 
                savedInvalidVote.getId(), savedInvalidVote.getUserId(), savedInvalidVote.getSectionId());
        return savedInvalidVote;
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
