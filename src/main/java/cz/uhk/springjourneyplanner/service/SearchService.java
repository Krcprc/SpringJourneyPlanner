package cz.uhk.springjourneyplanner.service;

import cz.uhk.springjourneyplanner.dto.*;
import cz.uhk.springjourneyplanner.engine.GraphParser;
import cz.uhk.springjourneyplanner.engine.PathFilter;
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

    //TODO vracet jiny objekt, co bude mit dobu trvani a za jak dlouho, a pak list(linka, odkud v kolik, kde v kolik)
    public List<Path> search(SearchParams params){
        List<LineDTO> allLines = lineService.getAllLineDTOs();
        List<String> stopNames = lineService.getAllDistinctStopNames();
        int transferTime = params.isAllowTransfers() ? params.getMinTransferTime() : 0;
        Map<StopTime, Node> graph = GraphParser.parseToGraph(stopNames, allLines, transferTime);
        Node start = Utils.findNode(graph, params.getFrom(), params.getTimeInt(), true);
        return findPaths(graph, start, params.getTo(), 5);
    }

    //TODO filtrovat kazdej balik cest od pathfinderu.
    // pri stejnym casu startu i cile a pri stejny lince zacatku i cile ukazat jen jeden s nejmensim poctem polozek
    private List<Path> findPaths(Map<StopTime, Node> graph, Node start, String end, int numberOfPaths){
        List<Path> paths = new ArrayList<>();
        while (paths.size() < numberOfPaths){
            if (paths.size() > 0){
                int time = paths.getLast().getConnections().getFirst().startTime() + 1;
                start = Utils.findNode(graph, start.getStop(), time, true);
            }
            List<Path> pathsFound = PathFinder.findPath(start, end);
            if (pathsFound == null){
                break;
            }
            paths.addAll(PathFilter.filterPaths(pathsFound));
        }
        return paths;
    }


    public boolean stopExists(String stopName){
        return stopRepository.existsStopByName(stopName);
    }

}

