package br.com.fiap.consciousbet.service;

import br.com.fiap.consciousbet.dto.AlertResponse;

public interface RiskAnalysisService {
    AlertResponse analyze(Long userId);
}
