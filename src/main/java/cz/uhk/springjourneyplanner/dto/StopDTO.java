package cz.uhk.springjourneyplanner.dto;

import java.util.Objects;

public class StopDTO {
    private String name;
    private Integer arr;
    private Integer dep;
    private String marker;

    public StopDTO(String name, Integer arr, Integer dep, String marker) {
        this.name = name;
        this.arr = arr;
        this.dep = dep;
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public Integer getArr() {
        return arr;
    }

    public Integer getDep() {
        return dep;
    }

    public String getMarker() {
        return marker;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setArr(Integer arr) {
        this.arr = arr;
    }

    public void setDep(Integer dep) {
        this.dep = dep;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
