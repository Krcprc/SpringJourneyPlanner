package cz.uhk.springjourneyplanner.repository;

import cz.uhk.springjourneyplanner.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

    List<Line> findAllByOrderByName();

}
