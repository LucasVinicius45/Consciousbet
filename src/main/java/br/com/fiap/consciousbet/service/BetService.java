package br.com.fiap.consciousbet.service;

import br.com.fiap.consciousbet.dto.BetCreateDTO;
import br.com.fiap.consciousbet.dto.BetResponseDTO;
import br.com.fiap.consciousbet.dto.BetUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BetService {

    // Criar aposta
    BetResponseDTO create(BetCreateDTO dto);

    // Buscar todas as apostas (com paginação)
    Page<BetResponseDTO> findAll(Pageable pageable);

    // Buscar todas as apostas (lista simples)
    List<BetResponseDTO> findAll();

    // Buscar por ID
    BetResponseDTO findById(Long id);

    // Buscar apostas por usuário
    List<BetResponseDTO> findByUserId(Long userId);

    // Buscar apostas por usuário com paginação
    Page<BetResponseDTO> findByUserId(Long userId, Pageable pageable);

    // Buscar apostas por tipo
    List<BetResponseDTO> findByType(String type);

    // Buscar apostas por status
    List<BetResponseDTO> findByStatus(String status);

    // Buscar apostas recentes de um usuário (últimas 24h)
    List<BetResponseDTO> findRecentBetsByUserId(Long userId);

    // Atualizar aposta
    BetResponseDTO update(Long id, BetUpdateDTO dto);

    // Atualizar status da aposta
    BetResponseDTO updateStatus(Long id, String status);

    // Deletar aposta (cancelar)
    void delete(Long id);

    // Cancelar aposta (soft delete - muda status)
    BetResponseDTO cancel(Long id);

    // Calcular total apostado por usuário
    BigDecimal getTotalAmountByUserId(Long userId);

    // Calcular total apostado por usuário em período
    BigDecimal getTotalAmountByUserIdSince(Long userId, LocalDateTime since);

    // Contar apostas por usuário
    long countByUserId(Long userId);

    // Contar apostas por usuário em período
    long countByUserIdSince(Long userId, LocalDateTime since);

    // Verificar se usuário pode apostar (regras de negócio)
    boolean canUserBet(Long userId, BigDecimal amount);
}