package br.com.fiap.consciousbet.dto;

public class AlertResponse {

    private boolean risk;
    private String message;
    private String suggestion;

    public AlertResponse() {}

    public AlertResponse(boolean risk, String message, String suggestion) {
        this.risk = risk;
        this.message = message;
        this.suggestion = suggestion;
    }

    public boolean isRisk() { return risk; }
    public void setRisk(boolean risk) { this.risk = risk; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
}
