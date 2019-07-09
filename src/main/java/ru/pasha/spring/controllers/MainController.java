package ru.pasha.spring.controllers;

import ru.pasha.spring.util.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.pasha.spring.service.DataFetchService;
import ru.pasha.spring.entity.Accuracy;
import ru.pasha.spring.repos.AccuracyRepo;
import ru.pasha.tosamara.classifiers.RouteClassifier;
import ru.pasha.tosamara.classifiers.StopClassifier;
import ru.pasha.tosamara.classifiers.xml.route.full.Route;
import ru.pasha.tosamara.classifiers.xml.stop.Stop;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Spring controller class
 */
@Controller
public class MainController {

    /**
     * Database repository. (<code> table = "accuracy.accuracy" </code>)
     */
    private final AccuracyRepo accuracyRepo;

    /**
     * @param accuracyRepo injected database repository
     */
    @Autowired
    public MainController(AccuracyRepo accuracyRepo) {
        this.accuracyRepo = accuracyRepo;
    }

    /**
     * Return routes going through the stop.
     * @param id stop id
     * @return HTML-text for combo box
     */
    @ResponseBody
    @GetMapping(path = "/getRouteByStop", produces = MediaType.TEXT_HTML_VALUE)
    public String getRouteList(@RequestParam(name = "stop_id") Integer id){
        if (id == null){
            return "";
        }
        List<Route> routes = new ArrayList<>();
        List<Long> routeIdList = accuracyRepo.selectDistinctRoutesByStopId(id);
        for (Long l : routeIdList){
            int routeId = Math.toIntExact(l);
            routes.add(RouteClassifier.findById(routeId));
        }
        if (routes.isEmpty()){
            return "";
        }
        StringBuilder b = new StringBuilder();
        // <option value="v">route number</option>
        for (Route r : routes){
            if (r != null){
                b.append("<option value=\"").append(r.getId()).append("\">");
                b.append(r.getNumber()).append(" ").append(r.getTransportType().toString());
                b.append("</option>\n");
            }
        }
        return b.toString();
    }

    /**
     * Return title of observed stops.
     * @return HTML-text for combo box
     * @see DataFetchService#OBSERVED_STOPS_ID
     */
    @ResponseBody
    @GetMapping(path = "/getObservedStops", produces = MediaType.TEXT_HTML_VALUE)
    public String getObservedStops(){
        StringBuilder b = new StringBuilder();
        // <option value="v">stop name</option>
        for (Integer id : DataFetchService.OBSERVED_STOPS_ID){
            Stop s = StopClassifier.findById(id);
            if (s != null) {
                b.append("<option value=\"").append(id).append("\">");
                b.append(s.getTitle());
                b.append("</option>\n");
            }
        }
        return b.toString();
    }

    /**
     * Return data for ChartJS graphic.
     * @param stopId stop identifier
     * @param routeId route identifier
     * @param startStr start time of observation
     * @param endStr end time of observation
     * @return JSON-array [{x: x1, y: y1}, {x: x2, y: y2}, ...]
     */
    @ResponseBody
    @GetMapping(path = "/getAccuracyData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<DataPoint> getData(
            @RequestParam(name = "stop_id") Integer stopId,
            @RequestParam(name = "route_id") Integer routeId,
            @RequestParam(name = "start") String startStr,
            @RequestParam(name = "end") String endStr){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Time start = null;
        Time end = null;
        Date temp;
        try {
            temp = dateFormat.parse(startStr);
            start = new Time(temp.getTime());
            temp = dateFormat.parse(endStr);
            end = new Time(temp.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (stopId == null || routeId == null || start == null || end == null){
            return new ArrayList<>();
        }
        List<Accuracy> accuracyList = accuracyRepo.findByRouteIdAndStopIdAndTimeBetween(routeId, stopId, start, end);
        List<DataPoint> data = new ArrayList<>(accuracyList.size());
        for (Accuracy a : accuracyList){
            data.add(new DataPoint(a.getRealTime(), a.getForecast()));
        }
        return data;
    }
}