package cz.uhk.springjourneyplanner.engine;

import cz.uhk.springjourneyplanner.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static int convertToMinutes(String time){
        String[] timeArr = time.split(":");
        String hours = timeArr[0];
        String minutes = timeArr[1];
        int timeMins = Integer.parseInt(minutes);
        timeMins += Integer.parseInt(hours) * 60;
        return timeMins;
    }

    public static String convertToReadableTime(int mins){
        int hours = mins / 60;
        int minutes = mins % 60;
        return hours % 24 + ":" + (minutes < 10 ? "0" + minutes : minutes) + (hours > 23 ? " zítra" : "");

    }

    public static List<Integer> convertToMinutes(List<String> list){
        return list.stream().map(Utils::convertToMinutes).toList();
    }

    /**
     * Converts timetable to list of departures in minutes for 2 sequential days
     * @param timetable list with 24 hourly departures in minutes separated by space
     * @return list of departures in minutes for 2 sequential days
     */
    //TODO vzit v potaz moznost markeru a zapornych casu
    public static List<Integer> timetableToConnections(String[] timetable){
        List<Integer> connections = new ArrayList<>(100);
        for (int i = 0; i < 48; i++){
            String hour = timetable[i % 24];
            if (hour == null || hour.length() == 0){
                continue;
            }
            String[] departures = hour.split(" ");
            for (int j = 0; j < departures.length; j++){
                try {
                    Integer departure = Integer.parseInt(departures[j]);
                    connections.add(i * 60 + departure);
                } catch (NumberFormatException e) {
                    //spolkneme...
                }
            }
        }
        return connections;
    }

    public static Node findNode(Map<StopTime, Node> graph, String stop, int time, boolean departing){
        return graph.get(new StopTime(stop, time, departing));
    }

    public static String pathToString(List<NodeLine> path){
        if (path == null){
            return "Spojení neexistuje";
        }
        StringBuilder sb = new StringBuilder(200);
        LineDTO line = null;
        for (NodeLine n : path){
            if (n == null) continue;
            //boolean lineChanged = line != n.line();
            line = n.line();

//            if (lineChanged){
                sb.append(n.node());
                sb.append(", ");
                sb.append(n.line() == null ? "čekání" : n.line().getName());
                sb.append("\n");
//            } else if (line != null){
//                sb.append(n.node());
//                sb.append("\n");
//            }

        }
        return sb.toString();
    }



}
