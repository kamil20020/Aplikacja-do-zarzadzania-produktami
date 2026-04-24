package pl.products.management.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.products.management.model.entity.RoleEntity;
import pl.products.management.model.entity.UserEntity;
import pl.products.management.repository.UserRepository;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public UserEntity getById(UUID id) throws EntityNotFoundException{

        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Nie odnaleziono użytkownika o id " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Nie odnaleziono użytkownika o loginie " + username));
    }

    @Transactional
    public UserEntity register(UserEntity userData) throws EntityExistsException{

        String username = userData.getUsername();

        if(userRepository.existsByUsername(username)){

            throw new EntityExistsException("Istnieje już użytkownik o nazwie " + username);
        }

        String rawPassword = userData.getPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        userData.setPassword(encryptedPassword);
        userData.setRoles(new HashSet<>());

        return userRepository.save(userData);
    }

    @Transactional
    public void assignRole(UUID userId, UUID roleId) throws EntityNotFoundException, EntityExistsException{

        UserEntity gotUser = getById(userId);
        RoleEntity gotRole = roleService.getById(roleId);

        if(userRepository.existsByIdAndRoles_Id(userId, roleId)){

            throw new EntityExistsException("Użytkownik " + gotUser.getUsername() + " ma już przypisaną rolę " + gotRole.getName());
        }

        gotUser.getRoles().add(gotRole);
    }
}
