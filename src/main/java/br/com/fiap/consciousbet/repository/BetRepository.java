package br.com.fiap.consciousbet.repository;

import br.com.fiap.consciousbet.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByUserId(Long userId);
    List<Bet> findByUserIdAndTimestampAfter(Long userId, LocalDateTime after);
}
