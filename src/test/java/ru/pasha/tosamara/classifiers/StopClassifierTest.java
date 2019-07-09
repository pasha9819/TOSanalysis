package ru.pasha.tosamara.classifiers;

import org.junit.Test;
import ru.pasha.tosamara.classifiers.grabbers.StopGrabber;
import ru.pasha.tosamara.classifiers.xml.stop.Stop;

import static org.junit.Assert.*;

public class StopClassifierTest {

    @Test
    public void findById() {
        new StopGrabber().downloadAndUpdate();

        Stop s = StopClassifier.findById(872);
        assertNotNull(s);
        assertNotNull(s.getId());
        assertNotNull(s.getTitle());
        assertEquals(java.util.Optional.of(872), java.util.Optional.of(s.getId()));
        assertEquals("Аэрокосмический Университет", s.getTitle());
    }
}