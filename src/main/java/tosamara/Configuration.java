package tosamara;

public abstract class Configuration {
    public static final String KEY_PATH = "src/main/resources/key.dat";

    public static final String LAST_STOPS_CLASSIFIER_PATH = "src/main/resources/classifiers/lastStops.xml";

    public static final String STOPS_CLASSIFIER_URL = "http://www.tosamara.ru/api/classifiers/stopsFullDB.xml";

    public static final String STOPS_CLASSIFIER_PATH = "src/main/resources/classifiers/stopsFullDB.xml";

    public static final String ROUTES_CLASSIFIER_URL = "http://www.tosamara.ru/api/classifiers/routesAndStopsCorrespondence.xml";

    public static final String ROUTES_CLASSIFIER_PATH = "src/main/resources/classifiers/routesAndStopsCorrespondence.xml";

    public static final String SIMPLE_ROUTES_URL = "http://www.tosamara.ru/api/classifiers/routes.xml";

    public static final String SIMPLE_ROUTES_PATH = "src/main/resources/classifiers/routes.xml";
}
