package spring.repos;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import spring.entity.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Component
public interface AccuracyRepo extends CrudRepository<Accuracy, AccuracyPK> {

    List<Accuracy> findByRouteIdAndStopIdAndTimeBetween(long routeId, long stopId, Time time, Time time2);
}
