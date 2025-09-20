package br.com.fiap.consciousbet.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BetCreateDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum bet amount is R$ 1.00")
    @DecimalMax(value = "10000.00", message = "Maximum bet amount is R$ 10,000.00")
    private BigDecimal amount;

    @NotBlank(message = "Bet type is required")
    @Pattern(regexp = "^(SPORTS|CASINO|LOTTERY|POKER)$", message = "Invalid bet type. Must be: SPORTS, CASINO, LOTTERY, or POKER")
    private String type;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    // Constructors
    public BetCreateDTO() {
    }

    public BetCreateDTO(Long userId, BigDecimal amount, String type, String description) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}