package pl.products.management.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.products.management.model.entity.RoleEntity;
import pl.products.management.repository.RoleRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity getById(UUID id) throws EntityNotFoundException{

        return roleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono roli o id " + id));
    }
}
