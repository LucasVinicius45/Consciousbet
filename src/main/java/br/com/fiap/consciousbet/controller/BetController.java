package br.com.fiap.consciousbet.controller;

import br.com.fiap.consciousbet.dto.BetDTO;
import br.com.fiap.consciousbet.dto.AlertResponse;
import br.com.fiap.consciousbet.entity.Bet;
import br.com.fiap.consciousbet.service.BetService;
import br.com.fiap.consciousbet.service.RiskAnalysisService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bets")
@SecurityRequirement(name = "bearerAuth")
public class BetController {

    private final BetService betService;
    private final RiskAnalysisService riskService;

    public BetController(BetService betService, RiskAnalysisService riskService) {
        this.betService = betService;
        this.riskService = riskService;
    }

    @PostMapping
    public Bet create(@RequestBody BetDTO dto) {
        return betService.create(dto);
    }

    @GetMapping
    public List<Bet> listAll() {
        return betService.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Bet> listByUser(@PathVariable Long userId) {
        return betService.findByUserId(userId);
    }

    @GetMapping("/alerts/{userId}")
    public AlertResponse analyze(@PathVariable Long userId) {
        return riskService.analyze(userId);
    }
}
