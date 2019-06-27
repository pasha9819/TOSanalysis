package tosamara.classifiers;

import tosamara.classifiers.xml.stop.Stop;

import java.util.List;

public class StopClassifier {
    public static List<Stop> stops;

    public static Stop findById(Integer KS_ID){
        int index = -1;
        for (int i = 0; i < stops.size(); i++)
            if (stops.get(i).getKS_ID().equals(KS_ID))
                index = i;
        if (index == -1)
            return null;
        return stops.get(index);
    }
}
