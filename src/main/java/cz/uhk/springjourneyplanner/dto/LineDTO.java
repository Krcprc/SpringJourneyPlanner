package cz.uhk.springjourneyplanner.dto;

import java.util.ArrayList;
import java.util.List;
import cz.uhk.springjourneyplanner.engine.Utils;

public class LineDTO {

    private Long id;

    private String name;

    private List<StopDTO> stops = new ArrayList<>();

    private String[] departures = new String[24];


    public LineDTO(){
        stops.add(new StopDTO("", 0, 0, ""));
        stops.add(new StopDTO("", 0, 0, ""));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StopDTO> getStops() {
        return stops;
    }

    public void setStops(List<StopDTO> stops) {
        this.stops = stops;
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

}
