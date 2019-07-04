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
    public String getRouteList(@RequestParam(name = "stop_id") Integer id){
        if (id == null){
            return "";
        }
        List<Route> routes = StopClassifier.getRoutesByStopId(id);
        if (routes.isEmpty()){
            return "";
        }
        StringBuilder b = new StringBuilder();
        // <option value="v">route number</option>
        for (Route r : routes){
            b.append("<option value=\"").append(r.getKR_ID()).append("\">");
            b.append(r.getNumber()).append(" ").append(r.getTransportType().toString());
            b.append("</option>\n");
        }
        return b.toString();
    }

}