package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.User;
import cntt2.k61.backend.dto.UserDto;
import cntt2.k61.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> checkLogin(UserDto userDto) {
        log.info("Checking login for user: {}", userDto);
        Optional<User> foundUser = userRepository.findByUserName(userDto.getUserName());
        if (foundUser.isPresent() && foundUser.get().getPassword().equals(userDto.getUserPassword())) {
            return foundUser;
        }
        return Optional.empty();
    }
}
