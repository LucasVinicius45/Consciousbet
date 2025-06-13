package br.com.fiap.consciousbet.serviceimpl;

import br.com.fiap.consciousbet.dto.AlertResponse;
import br.com.fiap.consciousbet.entity.Bet;
import br.com.fiap.consciousbet.repository.BetRepository;
import br.com.fiap.consciousbet.service.RiskAnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    private final BetRepository betRepo;

    public RiskAnalysisServiceImpl(BetRepository betRepo) {
        this.betRepo = betRepo;
    }

    @Override
    public AlertResponse analyze(Long userId) {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<Bet> recentBets = betRepo.findByUserIdAndTimestampAfter(userId, since);

        double total = recentBets.stream().mapToDouble(Bet::getAmount).sum();

        if (recentBets.size() >= 5 || total > 1000) {
            return new AlertResponse(
                    true,
                    "Você realizou " + recentBets.size() + " apostas nas últimas 24h.",
                    "Com R$" + total + ", você poderia investir em um CDB e garantir retorno mensal com liquidez diária."
            );
        }

        return new AlertResponse(false, "Nenhum comportamento de risco detectado.", null);
    }
}
