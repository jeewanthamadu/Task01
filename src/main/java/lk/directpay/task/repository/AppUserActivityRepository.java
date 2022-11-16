package lk.directpay.task.repository;


import lk.directpay.task.entity.AppUserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserActivityRepository extends JpaRepository<AppUserActivity, Integer> {
}
