package cz.uhk.springjourneyplanner.dto;

import cz.uhk.springjourneyplanner.engine.Utils;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final int duration, startTime, endTime;

    private final List<Connection> connections;

    private final List<String> lines;

    public Path(List<Connection> connections){
        this.connections = connections;
        startTime = connections.getFirst().startTime();
        endTime = connections.getLast().endTime();
        duration = endTime - startTime;
        lines = connections.stream().map(c -> c.line().getName()).toList();
    }

    public static Path toPath(List<NodeLine> nodeLines){
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < nodeLines.size(); i += 2){
            NodeLine start = nodeLines.get(i);
            NodeLine end = nodeLines.get(i + 1);
            connections.add(new Connection(start.line(), start.node().getStop(), end.node().getStop(),
                    start.node().getTime(), end.node().getTime()));
        }
        return new Path(connections);
    }

    public List<String> getLines(){
        return lines;
    }

    public String getReadableStartTime(){
        return Utils.convertToReadableTime(startTime);
    }

    public String getReadableEndTime(){
        return Utils.convertToReadableTime(endTime);
    }

    public int getDuration() {
        return duration;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
