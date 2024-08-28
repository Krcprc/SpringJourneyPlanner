package cz.uhk.springjourneyplanner.repository;

import cz.uhk.springjourneyplanner.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {


    boolean existsStopByName(String name);
}
