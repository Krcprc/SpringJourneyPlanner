package cz.uhk.springjourneyplanner.entity;

import jakarta.persistence.*;

@Entity
public class Connections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @Column
    private Integer hourNumber;
    @Column
    private String departures;

    public Connections(Line line, Integer hour, String departures) {
        this.line = line;
        this.hourNumber = hour;
        this.departures = departures;
    }

    public Connections() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Integer getHourNumber() {
        return hourNumber;
    }

    public void setHourNumber(Integer hour) {
        this.hourNumber = hour;
    }

    public String getDepartures() {
        return departures;
    }

    public void setDepartures(String departures) {
        this.departures = departures;
    }
}
