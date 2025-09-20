package br.com.fiap.consciousbet.serviceimpl;

import br.com.fiap.consciousbet.dto.AlertResponse;
import br.com.fiap.consciousbet.entity.Bet;
import br.com.fiap.consciousbet.repository.BetRepository;
import br.com.fiap.consciousbet.service.RiskAnalysisService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        // Usar BigDecimal em vez de double
        BigDecimal total = recentBets.stream()
                .map(Bet::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Converter para double apenas para comparação (ou usar BigDecimal.compareTo)
        double totalValue = total.doubleValue();
        int betCount = recentBets.size();

        // Critérios de risco mais específicos
        if (betCount >= 5 || totalValue > 1000) {
            String message = String.format(
                    "Comportamento de risco detectado: %d apostas totalizando R$ %.2f nas últimas 24 horas.",
                    betCount, totalValue
            );

            String suggestion = String.format(
                    "Com R$ %.2f, você poderia investir em um CDB que rende aproximadamente R$ %.2f por mês com liquidez diária.",
                    totalValue, totalValue * 0.01 // 1% ao mês como exemplo
            );

            return new AlertResponse(true, message, suggestion);
        }

        // Alerta preventivo para valores moderados
        if (betCount >= 3 || totalValue > 500) {
            String message = String.format(
                    "Atenção: %d apostas no valor de R$ %.2f hoje. Monitore seus gastos.",
                    betCount, totalValue
            );

            String suggestion = "Considere estabelecer limites diários para suas apostas e explore alternativas de investimento.";

            return new AlertResponse(false, message, suggestion);
        }

        return new AlertResponse(false, "Nenhum comportamento de risco detectado.",
                "Continue apostando com responsabilidade.");
    }
}