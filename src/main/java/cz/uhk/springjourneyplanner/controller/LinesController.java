package cz.uhk.springjourneyplanner.controller;
import cz.uhk.springjourneyplanner.dto.LineDTO;
import cz.uhk.springjourneyplanner.dto.StopDTO;
import cz.uhk.springjourneyplanner.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"line"})
public class LinesController {

    @Autowired
    private LineService lineService;

    @GetMapping("/lines")
    public String getLineList(Model model) {
        List<LineDTO> lines = lineService.getAllLineDTOs();
        model.addAttribute("lines", lines);
        return "lineList";
    }



    @GetMapping("/edit/{lineId}")
    public String editLine(@PathVariable("lineId") Long id, Model model) {
        LineDTO editedLine = lineService.getLineDTOById(id);
        model.addAttribute("line", editedLine);
        return "editLine";
    }

    @GetMapping("/delete/{lineId}")
    public String deleteLine(@PathVariable("lineId") Long id, Model model) {
        lineService.deleteLine(id);
        return "redirect:/lines";
    }

    @GetMapping("/new")
    public String newLine(Model model) {
        LineDTO lineDTO = new LineDTO();
        model.addAttribute("line", lineDTO);
        return "editLine";
    }

    @GetMapping("/edit")
    public String editLine(Model model) {
        List<String> stopNames = lineService.getAllDistinctStopNames();
        model.addAttribute("stopNames", stopNames);
        if (!model.containsAttribute("line")) {
            LineDTO lineDTO = new LineDTO();
            model.addAttribute("line", lineDTO);
        }
        return "editLine";
    }


    public String addRow(LineDTO lineDTO) {
        lineDTO.getStops().add(lineDTO.getStops().size(), new StopDTO("", 0, 0, ""));
        return "redirect:/edit";
    }

    public String removeRow(LineDTO lineDTO) {
        lineDTO.getStops().remove(lineDTO.getStops().size() - 1);
        return "redirect:/edit";
    }

    public String saveLine( LineDTO lineDTO) {
        lineService.saveLineDTO(lineDTO);
        return "redirect:/lines";
    }

    @PostMapping("/save")
    public String handleFormSubmit(@ModelAttribute("line") LineDTO lineDTO, @RequestParam("action") String action){
        if ("save".equals(action)){
            return saveLine(lineDTO);
        }
        if ("addRow".equals(action)){
            return addRow(lineDTO);
        }
        if ("removeRow".equals(action)){
            return removeRow(lineDTO);
        }
        return "redirect:/";
    }


}
