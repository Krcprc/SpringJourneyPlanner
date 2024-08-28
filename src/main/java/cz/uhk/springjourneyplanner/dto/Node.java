package cz.uhk.springjourneyplanner.dto;

import cz.uhk.springjourneyplanner.engine.Utils;

import java.util.List;

public class Node {

    private final String stop;

    private final int time;

    private final List<NodeLine> neighbors;

    private final boolean departing;


    public Node(String stop, int time, List<NodeLine> neighbors, boolean departing) {
        this.stop = stop;
        this.time = time;
        this.neighbors = neighbors;
        this.departing = departing;
    }

    public String getReadableTime(){
        return Utils.convertToReadableTime(time);
    }

    @Override
    public String toString(){
        return stop + " " + Utils.convertToReadableTime(time);
    }

    public void addNeighbor(Node node, LineDTO line){
        this.getNeighbors().add(new NodeLine(node, line));
    }

    public String getStop() {
        return stop;
    }

    public int getTime() {
        return time;
    }

    public List<NodeLine> getNeighbors() {
        return neighbors;
    }


    public boolean isDeparting() {
        return departing;
    }
}
