package cz.uhk.springjourneyplanner.dto;

import java.util.ArrayList;
import java.util.List;
import cz.uhk.springjourneyplanner.engine.Utils;

public class LineDTO {

    private Long id;

    private String name;

    private List<String> stops = new ArrayList<>(2);

    private List<Integer> minutesArr = new ArrayList<>(2);
    private List<Integer> minutesDep = new ArrayList<>(2);

    private String[] departures = new String[24];

    private List<Integer> connections;

    public LineDTO(){
        stops.add("");
        minutesArr.add(0);
        minutesDep.add(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    public List<Integer> getMinutesArr() {
        return minutesArr;
    }

    public void setMinutesArr(List<Integer> minutesArr) {
        this.minutesArr = minutesArr;
    }

    public List<Integer> getMinutesDep() {
        return minutesDep;
    }

    public void setMinutesDep(List<Integer> minutesDep) {
        this.minutesDep = minutesDep;
    }

    public String[] getDepartures() {
        return departures;
    }

    public void setDepartures(String[] departures) {
        this.departures = departures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getConnections() {
        if (connections == null){
            connections = Utils.timetableToConnections(departures);
        }
        return connections;
    }

    public void setConnections(List<Integer> connections) {
        this.connections = connections;
    }
}
