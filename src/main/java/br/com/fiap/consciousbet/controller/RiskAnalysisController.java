package br.com.fiap.consciousbet.controller;

import br.com.fiap.consciousbet.dto.AlertResponse;
import br.com.fiap.consciousbet.service.RiskAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bets/alerts")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Risk Analysis", description = "APIs for betting risk analysis and alerts")
public class RiskAnalysisController {

    private final RiskAnalysisService riskAnalysisService;

    public RiskAnalysisController(RiskAnalysisService riskAnalysisService) {
        this.riskAnalysisService = riskAnalysisService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Analyze user betting risk",
            description = "Analyzes user betting behavior and returns risk alerts with suggestions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Risk analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AlertResponse> analyzeUserRisk(@PathVariable Long userId) {
        System.out.println("GET /api/bets/alerts/" + userId + " - Analyzing risk for user");
        AlertResponse analysis = riskAnalysisService.analyze(userId);
        return ResponseEntity.ok(analysis);
    }
}