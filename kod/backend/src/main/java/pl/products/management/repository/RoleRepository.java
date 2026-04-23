package pl.products.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.products.management.model.entity.RoleEntity;

import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, UUID> {
}
