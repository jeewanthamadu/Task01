package lk.directpay.task.repository;

import lk.directpay.task.entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Integer> {


    List<AppUser> findByUsername(@Param("username") String username);
    AppUser findOneByUsername(@Param("username") String username);
    AppUser findAppUserByPhoneNumber(String phoneNumber);
    List<AppUser> findByEmail(@Param("email") String email);
    List<AppUser> findByPhoneNumber(@Param("phone_number") String phoneNumber);
    AppUser findByNic(@Param("nic") String nic);
    @Query(value = "SELECT * FROM APP_USER WHERE NIC LIKE ?1%", nativeQuery = true)
    AppUser findByOldNic(String nic);
    @Query(value = "SELECT FCM_TOKEN FROM APP_USER", nativeQuery = true)
    List<String> findAllFcmToken();

    @Query(value = "SELECT * FROM APP_USER WHERE NIC IN (?1, ?2)", nativeQuery = true)
    List<AppUser> findByNewNic(String newNicWith0, String newNicWith1);


}


