package br.com.fiap.consciousbet.controller;

import br.com.fiap.consciousbet.dto.BetCreateDTO;
import br.com.fiap.consciousbet.dto.BetResponseDTO;
import br.com.fiap.consciousbet.dto.BetUpdateDTO;
import br.com.fiap.consciousbet.service.BetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bets")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Bet Management", description = "APIs for managing bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    @Operation(summary = "Create a new bet", description = "Creates a new bet for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or betting limits exceeded"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<BetResponseDTO> createBet(@Valid @RequestBody BetCreateDTO dto) {
        System.out.println("POST /api/bets - Creating bet for user: " + dto.getUserId());
        BetResponseDTO createdBet = betService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBet);
    }

    @GetMapping
    @Operation(summary = "Get all bets", description = "Retrieves all bets with pagination support")
    @ApiResponse(responseCode = "200", description = "Bets retrieved successfully")
    public ResponseEntity<Page<BetResponseDTO>> getAllBets(
            @PageableDefault(size = 20, sort = "timestamp") Pageable pageable) {
        Page<BetResponseDTO> bets = betService.findAll(pageable);
        return ResponseEntity.ok(bets);
    }

    @GetMapping("/list")
    @Operation(summary = "Get all bets as list", description = "Retrieves all bets as a simple list")
    @ApiResponse(responseCode = "200", description = "Bets retrieved successfully")
    public ResponseEntity<List<BetResponseDTO>> getAllBetsList() {
        List<BetResponseDTO> bets = betService.findAll();
        return ResponseEntity.ok(bets);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bet by ID", description = "Retrieves a specific bet by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet found"),
            @ApiResponse(responseCode = "404", description = "Bet not found")
    })
    public ResponseEntity<BetResponseDTO> getBetById(@PathVariable Long id) {
        System.out.println("GET /api/bets/" + id);
        BetResponseDTO bet = betService.findById(id);
        return ResponseEntity.ok(bet);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get bets by user", description = "Retrieves all bets for a specific user")
    @ApiResponse(responseCode = "200", description = "User bets retrieved successfully")
    public ResponseEntity<List<BetResponseDTO>> getBetsByUserId(@PathVariable Long userId) {
        System.out.println("GET /api/bets/user/" + userId);
        List<BetResponseDTO> bets = betService.findByUserId(userId);
        return ResponseEntity.ok(bets);
    }

    @GetMapping("/user/{userId}/paginated")
    @Operation(summary = "Get bets by user with pagination", description = "Retrieves user bets with pagination")
    @ApiResponse(responseCode = "200", description = "User bets retrieved successfully")
    public ResponseEntity<Page<BetResponseDTO>> getBetsByUserIdPaginated(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "timestamp") Pageable pageable) {
        Page<BetResponseDTO> bets = betService.findByUserId(userId, pageable);
        return ResponseEntity.ok(bets);
    }

    @GetMapping("/user/{userId}/recent")
    @Operation(summary = "Get recent bets by user", description = "Retrieves user's bets from last 24 hours")
    @ApiResponse(responseCode = "200", description = "Recent bets retrieved successfully")
    public ResponseEntity<List<BetResponseDTO>> getRecentBetsByUserId(@PathVariable Long userId) {
        List<BetResponseDTO> bets = betService.findRecentBetsByUserId(userId);
        return ResponseEntity.ok(bets);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get bets by type", description = "Retrieves all bets of a specific type")
    @ApiResponse(responseCode = "200", description = "Bets retrieved successfully")
    public ResponseEntity<List<BetResponseDTO>> getBetsByType(@PathVariable String type) {
        List<BetResponseDTO> bets = betService.findByType(type);
        return ResponseEntity.ok(bets);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get bets by status", description = "Retrieves all bets with a specific status")
    @ApiResponse(responseCode = "200", description = "Bets retrieved successfully")
    public ResponseEntity<List<BetResponseDTO>> getBetsByStatus(@PathVariable String status) {
        List<BetResponseDTO> bets = betService.findByStatus(status);
        return ResponseEntity.ok(bets);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update bet", description = "Updates an existing bet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or bet cannot be updated"),
            @ApiResponse(responseCode = "404", description = "Bet not found")
    })
    public ResponseEntity<BetResponseDTO> updateBet(@PathVariable Long id,
                                                    @Valid @RequestBody BetUpdateDTO dto) {
        System.out.println("PUT /api/bets/" + id);
        BetResponseDTO updatedBet = betService.update(id, dto);
        return ResponseEntity.ok(updatedBet);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update bet", description = "Partially updates an existing bet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Bet not found")
    })
    public ResponseEntity<BetResponseDTO> partialUpdateBet(@PathVariable Long id,
                                                           @RequestBody BetUpdateDTO dto) {
        System.out.println("PATCH /api/bets/" + id);
        BetResponseDTO updatedBet = betService.update(id, dto);
        return ResponseEntity.ok(updatedBet);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update bet status", description = "Updates only the status of a bet")
    @ApiResponse(responseCode = "200", description = "Bet status updated successfully")
    public ResponseEntity<BetResponseDTO> updateBetStatus(@PathVariable Long id,
                                                          @RequestParam String status) {
        BetResponseDTO updatedBet = betService.updateStatus(id, status);
        return ResponseEntity.ok(updatedBet);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete bet", description = "Deletes an existing bet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Bet not found")
    })
    public ResponseEntity<Void> deleteBet(@PathVariable Long id) {
        System.out.println("DELETE /api/bets/" + id);
        betService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel bet", description = "Cancels a bet by updating its status")
    @ApiResponse(responseCode = "200", description = "Bet cancelled successfully")
    public ResponseEntity<BetResponseDTO> cancelBet(@PathVariable Long id) {
        BetResponseDTO cancelledBet = betService.cancel(id);
        return ResponseEntity.ok(cancelledBet);
    }

    @GetMapping("/user/{userId}/stats")
    @Operation(summary = "Get user betting statistics", description = "Retrieves betting statistics for a user")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getUserBettingStats(@PathVariable Long userId) {
        BigDecimal totalAmount = betService.getTotalAmountByUserId(userId);
        long totalBets = betService.countByUserId(userId);

        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
        BigDecimal dailyAmount = betService.getTotalAmountByUserIdSince(userId, yesterday);
        long dailyBets = betService.countByUserIdSince(userId, yesterday);

        Map<String, Object> stats = Map.of(
                "userId", userId,
                "totalAmount", totalAmount,
                "totalBets", totalBets,
                "dailyAmount", dailyAmount,
                "dailyBets", dailyBets,
                "averageBetAmount", totalBets > 0 ? totalAmount.divide(BigDecimal.valueOf(totalBets), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO
        );

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user/{userId}/can-bet")
    @Operation(summary = "Check if user can bet", description = "Checks if a user can place a bet of specified amount")
    @ApiResponse(responseCode = "200", description = "Check completed successfully")
    public ResponseEntity<Map<String, Object>> canUserBet(@PathVariable Long userId,
                                                          @RequestParam BigDecimal amount) {
        boolean canBet = betService.canUserBet(userId, amount);

        Map<String, Object> result = Map.of(
                "userId", userId,
                "requestedAmount", amount,
                "canBet", canBet
        );

        return ResponseEntity.ok(result);
    }
}