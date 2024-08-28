package cz.uhk.springjourneyplanner.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stop> stops;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Connections> connections;


    public Line(String name, List<Stop> stops, List<Connections> connections) {
        this.name = name;
        this.stops = stops;
        this.connections = connections;
    }

    public Line() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Connections> getConnections() {
        return connections;
    }

    public void setConnections(List<Connections> connections) {
        this.connections = connections;
    }
}


