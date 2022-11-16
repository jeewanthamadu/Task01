package lk.directpay.task.repository;


import lk.directpay.task.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
interface RolesRepository extends CrudRepository<Role, String> {
}
