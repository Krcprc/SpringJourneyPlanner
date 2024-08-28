package cz.uhk.springjourneyplanner.entity;

import jakarta.persistence.*;

@Entity
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @Column
    private String name;
    @Column
    private Integer minutesArr;
    @Column
    private Integer minutesDep;

    public Stop(Line line, String name, Integer minutesArr, Integer minutesDep) {
        this.line = line;
        this.name = name;
        this.minutesArr = minutesArr;
        this.minutesDep = minutesDep;
    }

    public Stop() {

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

    public Integer getMinutesArr() {
        return minutesArr;
    }

    public void setMinutesArr(Integer minutesArr) {
        this.minutesArr = minutesArr;
    }

    public Integer getMinutesDep() {
        return minutesDep;
    }

    public void setMinutesDep(Integer minutesDep) {
        this.minutesDep = minutesDep;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
