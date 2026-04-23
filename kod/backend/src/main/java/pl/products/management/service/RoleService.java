package pl.products.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.products.management.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
}
