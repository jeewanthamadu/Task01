package lk.directpay.task.repository;

import lk.directpay.task.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    static final int STATE_ACTIVE = 1;
    static final int STATE_DEACTIVATED = 0;

    List<Country> findAllByStateOrderByName(@Param("state") int state);

}
