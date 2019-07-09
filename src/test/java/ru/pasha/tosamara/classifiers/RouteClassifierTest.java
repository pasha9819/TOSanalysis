package ru.pasha.tosamara.classifiers;

import org.junit.Test;
import ru.pasha.tosamara.classifiers.grabbers.RouteGrabber;
import ru.pasha.tosamara.classifiers.xml.route.full.Route;

import static org.junit.Assert.*;

public class RouteClassifierTest {

    @Test
    public void findById() {
        new RouteGrabber().downloadAndUpdate();

        Route r = RouteClassifier.findById(85);
        assertNotNull(r);
        assertNotNull(r.getId());
        assertNotNull(r.getNumber());
        assertEquals(java.util.Optional.of(85), java.util.Optional.of(r.getId()));
        assertEquals("13", r.getNumber());
    }
}