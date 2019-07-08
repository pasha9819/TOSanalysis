package spring.controllers;

import accuracy.AccuracyTimer;
import accuracy.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.Application;
import spring.entity.Accuracy;
import spring.repos.AccuracyRepo;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.Updater;
import tosamara.classifiers.xml.route.full.Route;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Spring controller class
 */
@Controller
public class MainController {
    /**
     * Stops for which data is collected
     */
    private static final int[] observed = new int[]{872, 222, 218, 813, 1159, 1740, 329};

    /**
     * Database repository. (<code> table = "accuracy.accuracy" </code>)
     */
    private final AccuracyRepo accuracyRepo;

    /**
     * Create object, update classifiers and
     * start data fetching (if <code>Application.DATA_FETCHING == true</code>)
     * @param accuracyRepo injected database repository
     */
    @Autowired
    public MainController(AccuracyRepo accuracyRepo) {
        Updater.update(false);
        this.accuracyRepo = accuracyRepo;
        if (Application.DATA_FETCHING){
            try{
                for (int id : observed) {
                    new AccuracyTimer(id).start();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
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

    /**
     * Return title of observed stops.
     * @return HTML-text for combo box
     * @see MainController#observed
     */
    @ResponseBody
    @GetMapping(path = "/getObservedStops", produces = MediaType.TEXT_HTML_VALUE)
    public String getObservedStops(){
        StringBuilder b = new StringBuilder();
        // <option value="v">stop name</option>
        for (Integer id : observed){
            b.append("<option value=\"").append(id).append("\">");
            b.append(StopClassifier.findById(id).getTitle());
            b.append("</option>\n");
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
        Time start = parseTime(startStr);
        Time end = parseTime(endStr);
        if (stopId == null || routeId == null || start == null || end == null){
            return new ArrayList<>();
        }

        List<Accuracy> accuracyList = accuracyRepo.findByRouteIdAndStopIdAndTimeBetween(routeId, stopId, start, end);

        List<DataPoint> data = new ArrayList<>(accuracyList.size());
        for (Accuracy a : accuracyList){
            data.add(new DataPoint(a.getRealtime(), a.getForecast()));
        }
        return data;
    }

    /**
     * Parse <code>Time</code> from string.
     * @param str parsed string
     * @return Time object or null if unable to convert string
     */
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