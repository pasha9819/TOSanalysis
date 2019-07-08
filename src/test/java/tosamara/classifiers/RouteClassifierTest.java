package tosamara.classifiers;

import org.junit.Test;
import tosamara.classifiers.grabbers.RouteGrabber;
import tosamara.classifiers.xml.route.full.Route;

import static org.junit.Assert.*;

public class RouteClassifierTest {

    @Test
    public void findById() {
        new RouteGrabber().downloadAndUpdate();

        Route r = RouteClassifier.findById(85);
        assertNotNull(r);
        assertNotNull(r.getKR_ID());
        assertNotNull(r.getNumber());
        assertEquals(java.util.Optional.of(85), java.util.Optional.of(r.getKR_ID()));
        assertEquals("13", r.getNumber());
    }
}