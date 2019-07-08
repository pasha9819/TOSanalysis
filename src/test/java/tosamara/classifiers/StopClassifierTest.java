package tosamara.classifiers;

import org.junit.Test;
import tosamara.classifiers.grabbers.StopGrabber;
import tosamara.classifiers.xml.stop.Stop;

import static org.junit.Assert.*;

public class StopClassifierTest {

    @Test
    public void findById() {
        new StopGrabber().downloadAndUpdate();

        Stop s = StopClassifier.findById(872);
        assertNotNull(s);
        assertNotNull(s.getKS_ID());
        assertNotNull(s.getTitle());
        assertEquals(java.util.Optional.of(872), java.util.Optional.of(s.getKS_ID()));
        assertEquals("Аэрокосмический Университет", s.getTitle());
    }
}