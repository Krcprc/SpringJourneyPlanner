package cz.uhk.springjourneyplanner.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SearchParams {

    @NotBlank(message = "Stop name is required")
    private String from;

    @NotBlank(message = "Stop name is required")
    private String to;

    @NotNull(message = "Time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    private boolean allowTransfers;

    @Min(value = 1, message = "Minimum transfer time is 1 minute")
    private int minTransferTime;

    public SearchParams(String from, String to, LocalTime time, boolean allowTransfers, int minTransferTime) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.allowTransfers = allowTransfers;
        this.minTransferTime = minTransferTime;
    }

    public SearchParams(){
        time = LocalTime.now();
        allowTransfers = true;
        minTransferTime = 1;
    }

    public int getTimeInt(){
        return time.toSecondOfDay() / 60;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isAllowTransfers() {
        return allowTransfers;
    }

    public void setAllowTransfers(boolean allowTransfers) {
        this.allowTransfers = allowTransfers;
    }

    public int getMinTransferTime() {
        return minTransferTime;
    }

    public void setMinTransferTime(int minTransferTime) {
        this.minTransferTime = minTransferTime;
    }
}
