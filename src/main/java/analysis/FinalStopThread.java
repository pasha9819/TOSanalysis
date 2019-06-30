package analysis;

import io.IOUtil;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.API;
import tosamara.methods.json.ArrivalToStop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FinalStopThread extends Thread {
    private Integer KS_ID;

    FinalStopThread(Integer KS_ID) {
        this.KS_ID = KS_ID;
        setName("stop " + KS_ID + " thread");
        setDaemon(true);
    }

    public Integer getKS_ID() {
        return KS_ID;
    }

    public void setKS_ID(Integer KS_ID) {
        this.KS_ID = KS_ID;
    }

    @Override
    public void run() {

        List<String> prevList = new ArrayList<>();
        while (true){
            try{

                List<ArrivalToStop> list = API.getFirstArrivalToStop(new Stop(KS_ID));
                List<String> temp = new ArrayList<>();
                for (ArrivalToStop arrival : list){
                    if (arrival.remainingLength > 50 && arrival.spanLength - arrival.remainingLength > 50) {
                        continue;
                    }
                    StringBuilder b = new StringBuilder(100);
                    Route route = RouteClassifier.findById(arrival.KR_ID);
                    if (route == null) {
                        continue;
                    }
                    b.append(route.getTransportType().toString()).append(' ');
                    b.append(route.getNumber()).append(" маршрута(").append(arrival.stateNumber).append(") ");
                    Stop stop;
                    if (arrival.spanLength - arrival.remainingLength < 51){
                        stop = arrival.getPrevStop();
                        b.append("(-").append(arrival.spanLength - arrival.remainingLength).append(')');
                    }else {
                        stop = StopClassifier.findById(arrival.nextStopId);
                        b.append("(+").append(arrival.remainingLength).append(')');
                    }
                    b.append(" на остановке ");
                    if (stop == null) {
                        continue;
                    }
                    b.append(stop.getTitle());

                    temp.add(b.toString());
                    if (prevList.contains(b.toString())) {
                        continue;
                    }
                    b.insert(0,"-> " + KS_ID + ": " + arrival.date.get(Calendar.HOUR_OF_DAY) +
                            ':' + arrival.date.get(Calendar.MINUTE) + ':' + arrival.date.get(Calendar.SECOND) + ' ');
                    b.append("\n");
                    IOUtil.print(b.toString());
                }
                Thread.sleep(15000);
                prevList.clear();
                prevList.addAll(temp);
            }catch (Throwable e){
                e.printStackTrace();
            }

            Main.c++;
        }
    }


}
