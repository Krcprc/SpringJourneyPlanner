package cz.uhk.springjourneyplanner.service;

import cz.uhk.springjourneyplanner.dto.*;
import cz.uhk.springjourneyplanner.engine.GraphParser;
import cz.uhk.springjourneyplanner.engine.PathFinder;
import cz.uhk.springjourneyplanner.engine.Utils;
import cz.uhk.springjourneyplanner.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private LineService lineService;

    @Autowired
    private StopRepository stopRepository;

    public List<List<NodeLine>> search(SearchParams params){
        List<LineDTO> allLines = lineService.getAllLineDTOs();
        List<String> stopNames = lineService.getAllDistinctStopNames();
        int transferTime = params.isAllowTransfers() ? params.getMinTransferTime() : 0;
        Map<StopTime, Node> graph = GraphParser.parseToGraph(stopNames, allLines, transferTime);
        Node start = Utils.findNode(graph, params.getFrom(), params.getTimeInt(), true);
        return findPaths(graph, start, params.getTo(), 5);
    }

    private List<List<NodeLine>> findPaths(Map<StopTime, Node> graph, Node start, String end, int numberOfPaths){
        List<List<NodeLine>> paths = new ArrayList<>();
        while (paths.size() < numberOfPaths){
            if (paths.size() > 0){
                int time = paths.getLast().getFirst().node().getTime() + 1;
                start = Utils.findNode(graph, start.getStop(), time, true);
            }
            List<List<NodeLine>> pathsFound = PathFinder.findPath(start, end);
            if (pathsFound == null){
                break;
            }
            paths.addAll(pathsFound);
        }
        return paths;
    }


    public boolean stopExists(String stopName){
        return stopRepository.existsStopByName(stopName);
    }

}

