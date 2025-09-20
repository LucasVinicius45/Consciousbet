package br.com.fiap.consciousbet.serviceimpl;

import br.com.fiap.consciousbet.dto.BetCreateDTO;
import br.com.fiap.consciousbet.dto.BetResponseDTO;
import br.com.fiap.consciousbet.dto.BetUpdateDTO;
import br.com.fiap.consciousbet.entity.Bet;
import br.com.fiap.consciousbet.entity.User;
import br.com.fiap.consciousbet.mapper.BetMapper;
import br.com.fiap.consciousbet.repository.BetRepository;
import br.com.fiap.consciousbet.repository.UserRepository;
import br.com.fiap.consciousbet.service.BetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BetServiceImpl implements BetService {

    private final BetRepository betRepository;
    private final UserRepository userRepository;

    // Limites de segurança para apostas
    private static final BigDecimal MAX_DAILY_AMOUNT = new BigDecimal("5000.00");
    private static final int MAX_DAILY_BETS = 20;
    private static final BigDecimal MAX_SINGLE_BET = new BigDecimal("2000.00");

    public BetServiceImpl(BetRepository betRepository, UserRepository userRepository) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BetResponseDTO create(BetCreateDTO dto) {
        System.out.println("Creating bet for user ID: " + dto.getUserId() + ", amount: " + dto.getAmount());

        // Buscar usuário
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        // Validar regras de negócio
        validateBetCreation(dto.getUserId(), dto.getAmount());

        // Converter DTO para entidade
        Bet bet = BetMapper.toEntity(dto, user);

        // Salvar no banco
        Bet savedBet = betRepository.save(bet);
        System.out.println("Bet created with ID: " + savedBet.getId());

        return BetMapper.toResponseDTO(savedBet);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BetResponseDTO> findAll(Pageable pageable) {
        return betRepository.findAll(pageable)
                .map(BetMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BetResponseDTO> findAll() {
        return betRepository.findAll()
                .stream()
                .map(BetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BetResponseDTO findById(Long id) {
        Bet bet = betRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bet not found with ID: " + id));

        return BetMapper.toResponseDTO(bet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BetResponseDTO> findByUserId(Long userId) {
        return betRepository.findByUserId(userId)
                .stream()
                .map(BetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BetResponseDTO> findByUserId(Long userId, Pageable pageable) {
        return betRepository.findByUserId(userId, pageable)
                .map(BetMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BetResponseDTO> findByType(String type) {
        return betRepository.findByType(type.toUpperCase())
                .stream()
                .map(BetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BetResponseDTO> findByStatus(String status) {
        return betRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(BetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BetResponseDTO> findRecentBetsByUserId(Long userId) {
        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
        return betRepository.findByUserIdAndTimestampAfter(userId, yesterday)
                .stream()
                .map(BetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BetResponseDTO update(Long id, BetUpdateDTO dto) {
        System.out.println("Updating bet ID: " + id);

        Bet existingBet = betRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bet not found with ID: " + id));

        // Validar se a aposta pode ser atualizada
        if ("WON".equals(existingBet.getStatus()) || "LOST".equals(existingBet.getStatus())) {
            throw new IllegalStateException("Cannot update bet with status: " + existingBet.getStatus());
        }

        // Validar novo valor se fornecido
        if (dto.getAmount() != null) {
            validateBetAmount(dto.getAmount());
        }

        BetMapper.updateEntity(existingBet, dto);
        Bet updatedBet = betRepository.save(existingBet);

        System.out.println("Bet updated successfully");
        return BetMapper.toResponseDTO(updatedBet);
    }

    @Override
    @Transactional
    public BetResponseDTO updateStatus(Long id, String status) {
        Bet bet = betRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bet not found with ID: " + id));

        bet.setStatus(status.toUpperCase());
        Bet updatedBet = betRepository.save(bet);

        return BetMapper.toResponseDTO(updatedBet);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        System.out.println("Deleting bet ID: " + id);

        if (!betRepository.existsById(id)) {
            throw new EntityNotFoundException("Bet not found with ID: " + id);
        }

        betRepository.deleteById(id);
        System.out.println("Bet deleted successfully");
    }

    @Override
    @Transactional
    public BetResponseDTO cancel(Long id) {
        return updateStatus(id, "CANCELLED");
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByUserId(Long userId) {
        BigDecimal total = betRepository.sumAmountByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByUserIdSince(Long userId, LocalDateTime since) {
        BigDecimal total = betRepository.sumAmountByUserIdAndTimestampAfter(userId, since);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        return betRepository.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserIdSince(Long userId, LocalDateTime since) {
        return betRepository.countByUserIdAndTimestampAfter(userId, since);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserBet(Long userId, BigDecimal amount) {
        try {
            validateBetCreation(userId, amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos privados de validação
    private void validateBetCreation(Long userId, BigDecimal amount) {
        validateBetAmount(amount);
        validateDailyLimits(userId, amount);
    }

    private void validateBetAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Minimum bet amount is R$ 1.00");
        }
        if (amount.compareTo(MAX_SINGLE_BET) > 0) {
            throw new IllegalArgumentException("Maximum single bet amount is R$ " + MAX_SINGLE_BET);
        }
    }

    private void validateDailyLimits(Long userId, BigDecimal newAmount) {
        LocalDateTime today = LocalDateTime.now().minusHours(24);

        // Verificar limite diário de valor
        BigDecimal dailyTotal = getTotalAmountByUserIdSince(userId, today);
        if (dailyTotal.add(newAmount).compareTo(MAX_DAILY_AMOUNT) > 0) {
            throw new IllegalArgumentException("Daily betting limit exceeded. Limit: R$ " + MAX_DAILY_AMOUNT +
                    ", Current: R$ " + dailyTotal + ", Attempted: R$ " + newAmount);
        }

        // Verificar limite diário de número de apostas
        long dailyCount = countByUserIdSince(userId, today);
        if (dailyCount >= MAX_DAILY_BETS) {
            throw new IllegalArgumentException("Daily bet count limit exceeded. Limit: " + MAX_DAILY_BETS +
                    ", Current: " + dailyCount);
        }
    }
}