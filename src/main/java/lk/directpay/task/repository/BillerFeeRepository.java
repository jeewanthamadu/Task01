package lk.directpay.task.repository;


import lk.directpay.task.entity.BillerFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillerFeeRepository extends JpaRepository<BillerFee, Integer> {
    Optional<BillerFee> findOneByBillerId(String billerId);
}
