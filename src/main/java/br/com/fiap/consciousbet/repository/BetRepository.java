package br.com.fiap.consciousbet.repository;

import br.com.fiap.consciousbet.entity.Bet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    // Buscar apostas por usuário
    List<Bet> findByUserId(Long userId);

    // Buscar apostas por usuário com paginação
    Page<Bet> findByUserId(Long userId, Pageable pageable);

    // Buscar apostas por usuário após uma data específica
    List<Bet> findByUserIdAndTimestampAfter(Long userId, LocalDateTime after);

    // Buscar apostas por tipo
    List<Bet> findByType(String type);

    // Buscar apostas por status
    List<Bet> findByStatus(String status);

    // Buscar apostas por usuário e status
    List<Bet> findByUserIdAndStatus(Long userId, String status);

    // Buscar apostas por usuário e tipo
    List<Bet> findByUserIdAndType(Long userId, String type);

    // Queries customizadas
    @Query("SELECT b FROM Bet b WHERE b.amount >= :minAmount AND b.amount <= :maxAmount")
    List<Bet> findByAmountRange(@Param("minAmount") BigDecimal minAmount,
                                @Param("maxAmount") BigDecimal maxAmount);

    // Somar total apostado por usuário
    @Query("SELECT SUM(b.amount) FROM Bet b WHERE b.user.id = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") Long userId);

    // Somar total apostado por usuário em período específico
    @Query("SELECT SUM(b.amount) FROM Bet b WHERE b.user.id = :userId AND b.timestamp >= :startDate")
    BigDecimal sumAmountByUserIdAndTimestampAfter(@Param("userId") Long userId,
                                                  @Param("startDate") LocalDateTime startDate);

    // Contar apostas por usuário
    long countByUserId(Long userId);

    // Contar apostas por usuário em período específico
    @Query("SELECT COUNT(b) FROM Bet b WHERE b.user.id = :userId AND b.timestamp >= :startDate")
    long countByUserIdAndTimestampAfter(@Param("userId") Long userId,
                                        @Param("startDate") LocalDateTime startDate);

    // Buscar últimas N apostas de um usuário
    @Query("SELECT b FROM Bet b WHERE b.user.id = :userId ORDER BY b.timestamp DESC")
    Page<Bet> findLatestByUserId(@Param("userId") Long userId, Pageable pageable);

    // Buscar apostas com valor acima de um limite
    @Query("SELECT b FROM Bet b WHERE b.amount > :limit ORDER BY b.amount DESC")
    List<Bet> findHighValueBets(@Param("limit") BigDecimal limit);
}