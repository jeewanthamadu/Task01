package lk.directpay.task.repository;


import lk.directpay.task.entity.AppData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppDataRepository extends JpaRepository<AppData, Integer> {
    AppData findAppDataByPlatformAndBuildNumber(String platform, int buildNumber);
    AppData findAppDataByPlatformAndBuildNumberAndSignature(String platform, int buildNumber,String signature);
}
