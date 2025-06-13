package br.com.fiap.consciousbet.service;

import br.com.fiap.consciousbet.dto.BetDTO;
import br.com.fiap.consciousbet.entity.Bet;

import java.util.List;

public interface BetService {
    Bet create(BetDTO dto);
    List<Bet> findAll();
    List<Bet> findByUserId(Long userId);
}
