package pl.products.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.products.management.model.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}
