package cz.uhk.springjourneyplanner.controller;
import cz.uhk.springjourneyplanner.dto.LineDTO;
import cz.uhk.springjourneyplanner.dto.NodeLine;
import cz.uhk.springjourneyplanner.dto.SearchParams;
import cz.uhk.springjourneyplanner.service.LineService;
import cz.uhk.springjourneyplanner.service.SearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"searchParams"})
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private LineService lineService;

    @GetMapping("/")
    public String index(Model model) {
        if (!model.containsAttribute("searchParams")) {
            SearchParams searchParams = new SearchParams();
            model.addAttribute("searchParams", searchParams);
        }
        List<String> stopNames = lineService.getAllDistinctStopNames();
        model.addAttribute("stopNames", stopNames);
        return "index";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("searchParams") @Valid SearchParams params, BindingResult bindingResult, Model model){
        if (!validateStop(params.getFrom())){
            bindingResult.addError(new FieldError("searchParams", "from",
                    "Stop " + params.getFrom()+ " does not exist"));
        }
        if (!validateStop(params.getTo())){
            bindingResult.addError(new FieldError("searchParams", "to", "Stop " + params.getTo()+ " does not exist"));
        }
        if (params.getFrom().equals(params.getTo())){
            bindingResult.addError(new FieldError("searchParams", "to", "Stop overlap"));
        }
        if (bindingResult.hasErrors()) {
            return "index";
        }
        List<List<NodeLine>> paths = searchService.search(params);
        model.addAttribute("paths", paths);
        return "searchResults";
    }

    private boolean validateStop(String stopName){
        return searchService.stopExists(stopName);
    }

    @GetMapping("/newSearch")
    private String newSearch(Model model){
        SearchParams searchParams = new SearchParams();
        model.addAttribute("searchParams", searchParams);
        List<String> stopNames = lineService.getAllDistinctStopNames();
        model.addAttribute("stopNames", stopNames);
        return "index";
    }


}
