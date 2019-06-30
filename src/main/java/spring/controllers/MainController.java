package spring.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static tosamara.classifiers.xml.route.full.Route.TransportType.*;

@Controller
public class MainController {

    @GetMapping("/stop")
    public String stopInfo(
            @RequestParam(name="id", defaultValue = "1")
            Integer id,
            Model model) {
        model.addAttribute("stop", StopClassifier.findById(id));
        return "stop";
    }

    @ResponseBody
    @GetMapping(path = "/analysis", produces = MediaType.TEXT_HTML_VALUE)
    public String getRouteList(
            @RequestParam(name = "stop_id")
            Integer id,
            Model model){
        if (id == null){
            return "";
        }
        Stop stop = StopClassifier.findById(id);
        if (stop == null){
            return "";
        }
        String[] busNumbers = stop.getBusesMunicipal().split(", ");
        String[] tramNumbers = stop.getTrams().split(", ");
        String[] trolNumbers = stop.getTrolleybuses().split(", ");

        List<Route> routes = new ArrayList<>();
        for(String s : busNumbers){
            Route r = RouteClassifier.findByNumber(s, BUS);
            if (r != null){
                routes.add(r);
            }
        }
        for(String s : tramNumbers){
            Route r = RouteClassifier.findByNumber(s, TRAM);
            if (r != null){
                routes.add(r);
            }
        }
        for(String s : trolNumbers){
            Route r = RouteClassifier.findByNumber(s, TROL);
            if (r != null){
                routes.add(r);
            }
        }
        if (routes.isEmpty()){
            return "";
        }
        StringBuilder b = new StringBuilder();
        // <option value="2">Пункт 2</option>
        for (Route r : routes){
            b.append("<option value=\"").append(r.getKR_ID()).append("\">");
            b.append(r.getNumber()).append(" ").append(r.getTransportType().toString());
            b.append("</option>\n");
        }
        return b.toString();
    }

}