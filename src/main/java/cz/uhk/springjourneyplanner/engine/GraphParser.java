package cz.uhk.springjourneyplanner.engine;

import cz.uhk.springjourneyplanner.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphParser {


    public static Map<StopTime, Node> parseToGraph(List<String> stops, List<LineDTO> lines, int minTransferTime){
        Map<StopTime, Node> allNodes = initGraph(stops, minTransferTime);
        addConnectionNeighbors(lines, allNodes);
        return allNodes;
    }

    /**
     * zalozi pro kazdou "zastavkominutu" dva vrcholy - prijezdovy a odjezdovy pro 2 po sobe jdouci dny
     * @param stops vsechny zastavky
     * @param minTransferTime minimalni cas na prestup. Pokud je 0, prestupy nebudou umozneny
     * @return mapa vsech vrcholu
     */
    private static Map<StopTime, Node> initGraph(List<String> stops, int minTransferTime){
        Map<StopTime, Node> allNodes = new HashMap<>();
        for (int i = 1440 * 2; i >= 0; i--){
            for (String s : stops){
                Node nodeDeparting = new Node(s, i, new ArrayList<>(), true);
                Node nodeArriving = new Node(s, i, new ArrayList<>(), false);
                StopTime keyDeparting = new StopTime(s, i, true);
                StopTime keyArriving = new StopTime(s, i, false);
                allNodes.put(keyDeparting, nodeDeparting);
                allNodes.put(keyArriving, nodeArriving);

                addWaitingEdges(nodeDeparting, allNodes, i);
                addTransferEdges(nodeArriving, allNodes, i, minTransferTime);
            }
        }
        return allNodes;
    }

    /**
     * prida propojku mezi odjezdovym vrcholem a odjezdovym vrcholem stejne zastavky o minutu pozdeji
     * (umozni cekani v zastavce)
     */
    private static void addWaitingEdges(Node nodeDeparting,  Map<StopTime, Node> allNodes, int time){
        Node nextMinuteNode = allNodes.get(new StopTime(nodeDeparting.getStop(), time + 1, true));
        if (nextMinuteNode != null){
            nodeDeparting.addNeighbor(nextMinuteNode, null);
        }
    }

    /**
     * prida propojku mezi prijezdovym a odjezdovym vrcholem o cas prestupu pozdeji teze zastavky
     * (umozni nebo znemozni prestup)
     */
    private static void addTransferEdges(Node nodeArriving,  Map<StopTime, Node> allNodes, int time, int minTransferTime){
        if (minTransferTime > 0){
            StopTime key = new StopTime(nodeArriving.getStop(), time + minTransferTime, true);
            Node transferNode = allNodes.get(key);
            if (transferNode != null){
                nodeArriving.addNeighbor(transferNode, null);
            }
        }
    }

    /**
     * pro kazdy spoj kazde linky vznikne hrana z kazde zastavky na kazdou
     * @param lines vsechny linky
     * @param allNodes graf
     */
    private static void addConnectionNeighbors(List<LineDTO> lines, Map<StopTime, Node> allNodes){
        for (LineDTO line : lines){
            for (int i = 0; i < 48; i++){
                String departuresByHour = line.getDepartures()[i % 24];
                List<List<ConnectionPart>> connections = parseHour(departuresByHour, i, line);
                connections.forEach(c -> addEdges(c, allNodes, line));
            }
        }
    }

    private static void addEdges(List<ConnectionPart> connection, Map<StopTime, Node> allNodes, LineDTO line){
        for (int i = 0; i < connection.size() - 1 ; i++){
            ConnectionPart starting = connection.get(i);
            for (int j = i + 1; j < connection.size(); j++){
                ConnectionPart ending = connection.get(j);
                Node startingNode = Utils.findNode(allNodes, starting.stop(), starting.dep(), true);
                Node endingNode = Utils.findNode(allNodes, ending.stop(), ending.arr(), false);
                startingNode.addNeighbor(endingNode, line);
            }
        }
    }




    private static List<List<ConnectionPart>> parseHour(String connectionsPerHour, int hour, LineDTO line){
        List<List<ConnectionPart>> list = new ArrayList<>();
        if (connectionsPerHour == null || connectionsPerHour.length() < 2){
            return list;
        }
        String[] departures = connectionsPerHour.split(" ");
        for (int j = 0; j < departures.length; j++){
            try {
                Integer departure = Integer.parseInt(departures[j].substring(0,2));
                String markers = departures[j].length() > 2 ? departures[j].substring(2) : "";
                list.add(handleConnectionMarkers(departure + 60 * hour, markers, line));
            } catch (NumberFormatException e) {
                //spolkneme...
            }
        }
        return list;
    }

    public static List<ConnectionPart> handleConnectionMarkers(int departure, String markers, LineDTO dto){
        List<ConnectionPart> conn = new ArrayList<>();
        for (StopDTO stop : dto.getStops()){
            if (stop.getMarker() == null || (markers != null && markers.contains(stop.getMarker()))){
                conn.add(new ConnectionPart(stop.getName(), departure + stop.getArr(), departure + stop.getDep()));
            }
        }
        return conn;
    }
}
