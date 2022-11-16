package lk.directpay.task.repository;


import lk.directpay.task.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Integer> {
    Parameter findParameterByName(String name);
}
