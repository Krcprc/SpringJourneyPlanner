package cz.uhk.springjourneyplanner.dto;

import cz.uhk.springjourneyplanner.engine.Utils;

public record Connection(LineDTO line, String start, String end, int startTime, int endTime) {

    public String getReadableStartTime(){
        return Utils.convertToReadableTime(startTime);
    }

    public String getReadableEndTime(){
        return Utils.convertToReadableTime(endTime);
    }
}
