package accuracy;

import accuracy.entity.Accuracy;
import accuracy.entity.AccuracyPK;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface AccuracyRepo extends CrudRepository<Accuracy, AccuracyPK> {

    List<Accuracy> findByRouteIdAndStopIdAndTimeBetween(long routeId, long stopId, Date time, Date time2);
}
