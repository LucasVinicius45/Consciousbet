package br.com.fiap.consciousbet.dto;

public class BetDTO {

    private Double amount;
    private String type;
    private Long userId;

    public BetDTO() {}

    public BetDTO(Double amount, String type, Long userId) {
        this.amount = amount;
        this.type = type;
        this.userId = userId;
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
