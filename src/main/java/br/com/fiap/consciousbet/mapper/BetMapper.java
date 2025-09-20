package br.com.fiap.consciousbet.mapper;

import br.com.fiap.consciousbet.dto.BetCreateDTO;
import br.com.fiap.consciousbet.dto.BetResponseDTO;
import br.com.fiap.consciousbet.dto.BetUpdateDTO;
import br.com.fiap.consciousbet.entity.Bet;
import br.com.fiap.consciousbet.entity.User;

public class BetMapper {

    // DTO de criação para entidade
    public static Bet toEntity(BetCreateDTO dto, User user) {
        Bet bet = new Bet();
        bet.setAmount(dto.getAmount());
        bet.setType(dto.getType().toUpperCase());
        bet.setDescription(dto.getDescription());
        bet.setUser(user);
        bet.setStatus("PENDING");
        return bet;
    }

    // Entidade para DTO de resposta
    public static BetResponseDTO toResponseDTO(Bet bet) {
        return new BetResponseDTO(
                bet.getId(),
                bet.getUser().getId(),
                bet.getUser().getName(),
                bet.getUser().getEmail(),
                bet.getAmount(),
                bet.getType(),
                bet.getDescription(),
                bet.getStatus(),
                bet.getTimestamp()
        );
    }

    // Atualizar entidade com DTO de atualização (apenas campos não nulos)
    public static void updateEntity(Bet bet, BetUpdateDTO dto) {
        if (dto.getAmount() != null) {
            bet.setAmount(dto.getAmount());
        }
        if (dto.getType() != null && !dto.getType().trim().isEmpty()) {
            bet.setType(dto.getType().trim().toUpperCase());
        }
        if (dto.getDescription() != null) {
            bet.setDescription(dto.getDescription().trim());
        }
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            bet.setStatus(dto.getStatus().trim().toUpperCase());
        }
    }
}