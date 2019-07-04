package spring.controllers;

import spring.repos.AccuracyRepo;
import accuracy.StopThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.Updater;
import tosamara.classifiers.xml.route.full.Route;

import java.util.List;

@Controller
public class MainController {

    private final AccuracyRepo accuracyRepo;

    @Autowired
    public MainController(AccuracyRepo accuracyRepo) {
        Updater.update(false);
        this.accuracyRepo = accuracyRepo;
        int[] stopsId = new int[]{872, 222, 218, 813, 1159, 1740, 329};
        for (int id : stopsId) {
            StopThread t = new StopThread(id);
            t.start();
        }
    }




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