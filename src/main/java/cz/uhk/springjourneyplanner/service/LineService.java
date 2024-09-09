package cz.uhk.springjourneyplanner.service;

import cz.uhk.springjourneyplanner.dto.LineDTO;
import cz.uhk.springjourneyplanner.dto.StopDTO;
import cz.uhk.springjourneyplanner.entity.Connections;
import cz.uhk.springjourneyplanner.entity.Line;
import cz.uhk.springjourneyplanner.entity.Stop;
import cz.uhk.springjourneyplanner.repository.LineRepository;
import cz.uhk.springjourneyplanner.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LineService {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StopRepository stopRepository;

    public List<String> getAllDistinctStopNames() {
        return stopRepository.findAll().stream().map(Stop::getName).distinct().toList();
    }


    public List<LineDTO> getAllLineDTOs() {
        List<Line> lines = lineRepository.findAllByOrderByName();
        List<LineDTO> lineDTOs = new ArrayList<>();
        for (Line line : lines){
            lineDTOs.add(parseLineEntity(line));
        }
        return lineDTOs;
    }

    public LineDTO getLineDTOById(Long id){
        Optional<Line> optLine = lineRepository.findById(id);
        if (optLine.isEmpty()) return null;
        Line line = optLine.get();
        return parseLineEntity(line);
    }

    private LineDTO parseLineEntity(Line line){
        LineDTO lineDTO = new LineDTO();
        lineDTO.setId(line.getId());
        lineDTO.setName(line.getName());
        lineDTO.setDepartures(
                line.getConnections().stream()
                        .sorted(Comparator.comparingInt(Connections::getHourNumber))
                        .map(Connections::getDepartures).toArray(String[]::new)
        );
        //FIXME zajistit spravne zobrazovani zastavek (mozna bez comparu)?
        lineDTO.setStops(
                line.getStops().stream()
                        .sorted(Comparator.comparingInt(Stop::getMinutesDep))
                        .map(stop -> new StopDTO(stop.getName(), stop.getMinutesArr(), stop.getMinutesDep(), stop.getMarkers()))
                        .collect(Collectors.toCollection(ArrayList::new))
        );
        return lineDTO;
    }

    public void saveLineDTO(LineDTO dto){
        if (dto.getId() != null){
            lineRepository.deleteById(dto.getId());
        }
        List<Stop> stops = new ArrayList<>();
        List<Connections> connections = new ArrayList<>();
        Line line = new Line(dto.getName(), stops, connections);

        dto.getStops().stream()
                .map(stop -> new Stop(line, stop.getName(), stop.getArr(), stop.getDep(), stop.getMarker()))
                .forEach(stops::add);

        for (int i = 0; i < dto.getDepartures().length; i++){
            Connections connection = new Connections(line, i, dto.getDepartures()[i]);
            connections.add(connection);
        }

        lineRepository.save(line);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}

