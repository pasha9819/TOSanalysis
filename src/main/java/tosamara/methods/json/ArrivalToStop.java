package tosamara.methods.json;

import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class ArrivalToStop {
    public Calendar date = new GregorianCalendar();
    public String stateNumber;
    public Integer KR_ID;
    public Integer hullNo;
    public Integer nextStopId;
    public Double spanLength;
    public Double remainingLength;
    public Double timeInSeconds;
    public Stop stop;

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(" date = ").append(date.getTime());
        b.append(" KR_ID = ").append(KR_ID);
        b.append(" hullNo = ").append(hullNo);
        b.append(" nextStopId = ").append(nextStopId);
        return b.toString();
    }

    private Integer getPrevStopID(){
        Route route = RouteClassifier.findById(KR_ID);
        if (route == null) {
            return null;
        }
        for (int i = 1; i < route.getStops().size(); i++)
            if (route.getStops().get(i).getKS_ID().equals(nextStopId))
                return route.getStops().get(i - 1).getKS_ID();
        return null;
    }

    public Stop getPrevStop(){
        Integer id = getPrevStopID();
        if (id != null){
            return StopClassifier.findById(id);
        }
        return null;
    }

    public boolean isTransportNearSomeStop(){
        List<Route.Stop> stops = RouteClassifier.findById(KR_ID).getStops();
        if (Objects.equals(nextStopId, stops.get(stops.size() - 1).getKS_ID())){
            return remainingLength < 70 || spanLength - remainingLength < 30;
        }
        return remainingLength < 50 || spanLength - remainingLength < 30;
    }


}
