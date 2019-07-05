package spring.controllers;

import accuracy.AccuracyThread;
import accuracy.DataPoint;
import spring.entity.Accuracy;
import spring.repos.AccuracyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.Updater;
import tosamara.classifiers.xml.route.full.Route;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Controller
public class MainController {

    private final AccuracyRepo accuracyRepo;

    @Autowired
    public MainController(AccuracyRepo accuracyRepo) {
        Updater.update(false);
        this.accuracyRepo = accuracyRepo;
        for (int id : AccuracyThread.observed) {
            AccuracyThread t = new AccuracyThread(id);
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
    @GetMapping(path = "/getRouteByStop", produces = MediaType.TEXT_HTML_VALUE)
    public String getRouteList(@RequestParam(name = "stop_id") Integer id){
        if (id == null){
            return "";
        }
        List<Route> routes;
        //routes = StopClassifier.getRoutesByStopId(id);
        routes = new ArrayList<>();
        List<Long> routeIdList = accuracyRepo.selectDistinctRoutesByStopId(id);
        for (Long l : routeIdList){
            Integer routeId = Math.toIntExact(l);
            routes.add(RouteClassifier.findById(routeId));
        }
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

    @ResponseBody
    @GetMapping(path = "/getObservedStops", produces = MediaType.TEXT_HTML_VALUE)
    public String getObservedStops(){
        StringBuilder b = new StringBuilder();
        // <option value="v">stop name</option>
        for (Integer id : AccuracyThread.observed){
            b.append("<option value=\"").append(id).append("\">");
            b.append(StopClassifier.findById(id).getTitle());
            b.append("</option>\n");
        }
        return b.toString();
    }

    @ResponseBody
    @GetMapping(path = "/getAccuracyData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<DataPoint> getData(
            @RequestParam(name = "stop_id") Integer stopId,
            @RequestParam(name = "route_id") Integer routeId,
            @RequestParam(name = "start") String startStr,
            @RequestParam(name = "end") String endStr){
        DataPoint.ListWrapper answer = new DataPoint.ListWrapper();
        if (stopId == null || routeId == null || startStr == null || endStr == null){
            return answer.getPoints();
        }
        Time start = parseTime(startStr);
        Time end = parseTime(endStr);
        List<Accuracy> accuracyList = accuracyRepo.findByRouteIdAndStopIdAndTimeBetween(routeId, stopId, start, end);
        //accuracyList = accuracyRepo.findByStopId(stopId);

        List<DataPoint> data = new ArrayList<>(accuracyList.size());
        double x,y;
        for (Accuracy a : accuracyList){
            x = a.getRealtime();
            y = a.getForecast() - x;
            data.add(new DataPoint(x,y));
        }
        answer.setPoints(data);
        return data;
    }

    private Time parseTime(String str){
        if (str == null){
            return null;
        }
        String[] temp = str.split(":");
        if (temp.length != 3){
            return null;
        }
        long h = Integer.parseInt(temp[0]);
        long m = Integer.parseInt(temp[1]);
        long s = Integer.parseInt(temp[2]);
        m += h * 60;
        s += m * 60;
        TimeZone tz = TimeZone.getDefault();
        return new Time(s * 1000 - tz.getRawOffset());
    }

}