package cz.uhk.springjourneyplanner.dto;

import java.util.Objects;

public class StopDTO {
    private String name;
    private Integer arr;
    private Integer dep;
    private String markers;

    public StopDTO(String name, Integer arr, Integer dep, String markers) {
        this.name = name;
        this.arr = arr;
        this.dep = dep;
        this.markers = markers;
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

    public String getMarkers() {
        return markers;
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

    public void setMarkers(String markers) {
        this.markers = markers;
    }
}
