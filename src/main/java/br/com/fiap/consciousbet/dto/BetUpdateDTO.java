package br.com.fiap.consciousbet.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BetUpdateDTO {

    @DecimalMin(value = "1.00", message = "Minimum bet amount is R$ 1.00")
    @DecimalMax(value = "10000.00", message = "Maximum bet amount is R$ 10,000.00")
    private BigDecimal amount;

    @Pattern(regexp = "^(SPORTS|CASINO|LOTTERY|POKER)$", message = "Invalid bet type. Must be: SPORTS, CASINO, LOTTERY, or POKER")
    private String type;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Pattern(regexp = "^(PENDING|ACTIVE|WON|LOST|CANCELLED)$", message = "Invalid status. Must be: PENDING, ACTIVE, WON, LOST, or CANCELLED")
    private String status;

    // Constructors
    public BetUpdateDTO() {
    }

    public BetUpdateDTO(BigDecimal amount, String type, String description, String status) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    // Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}