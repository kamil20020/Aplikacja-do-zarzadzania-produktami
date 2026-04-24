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
import pl.products.management.model.entity.UserEntity;
import pl.products.management.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        return userRepository.save(userData);
    }
}
