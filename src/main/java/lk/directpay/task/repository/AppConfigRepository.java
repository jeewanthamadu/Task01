package lk.directpay.task.repository;

import lk.directpay.task.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
    AppConfig findAppConfigByPlatformIgnoreCase(String platform);
}
