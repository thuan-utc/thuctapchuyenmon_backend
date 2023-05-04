package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.Customer;
import cntt2.k61.backend.domain.User;
import cntt2.k61.backend.domain.UserRole;
import cntt2.k61.backend.dto.SignupDto;
import cntt2.k61.backend.dto.UserDto;
import cntt2.k61.backend.repository.CustomerRepository;
import cntt2.k61.backend.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public UserService(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public Optional<User> checkLogin(UserDto userDto) {
        log.info("Checking login for user: {}", userDto);
        Optional<User> foundUser = userRepository.findByUserName(userDto.getUserName());
        if (foundUser.isPresent() && foundUser.get().getPassword().equals(userDto.getUserPassword())) {
            return foundUser;
        }
        return Optional.empty();
    }

    @Transactional
    public String processSignup(SignupDto signupDto) {
        if (StringUtils.isBlank(signupDto.getEmail()) || StringUtils.isBlank(signupDto.getPhone())
            || StringUtils.isBlank(signupDto.getAddress()) || StringUtils.isBlank(signupDto.getPassword())
            || StringUtils.isBlank(signupDto.getUserName()) || StringUtils.isBlank(signupDto.getName())) {
            return "Please fill all input";
        }
        Optional<User> existingUser = userRepository.findByUserName(signupDto.getUserName());
        if (existingUser.isPresent()) {
            return "User name has already used";
        }
        Optional<Customer> existingCustomer = customerRepository.findAllByPhoneNumber(signupDto.getPhone());
        if (existingCustomer.isPresent()) {
            return "Phone number has already used";
        }
        Optional<User> existingEmailUser = userRepository.findAllByEmail(signupDto.getEmail());
        if (existingEmailUser.isPresent()) {
            return "Email has already used";
        }
        if (!isValidPhoneNumber(signupDto.getPhone())) {
            return "Phone number not valid";
        }
        if (!isValidEmail(signupDto.getEmail())) {
            return "Email not valid";
        }
        Customer newCustomer = new Customer();
        newCustomer.setAddress(signupDto.getAddress());
        newCustomer.setName(signupDto.getName());
        newCustomer.setPhoneNumber(signupDto.getPhone());
        newCustomer.setCreatedDate(LocalDateTime.now());
        newCustomer = customerRepository.save(newCustomer);
        User newUser = new User();
        newUser.setUserName(signupDto.getUserName());
        newUser.setEmail(signupDto.getEmail());
        newUser.setPassword(signupDto.getPassword());
        newUser.setRole(UserRole.user);
        newUser.setCustomer(newCustomer);
        newUser.setCreatedDate(LocalDateTime.now());
        userRepository.save(newUser);
        return "SUCCESS";
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "\\d{10}";
        Pattern pattern = Pattern.compile(phoneRegex);
        if (phoneNumber == null)
            return false;
        return pattern.matcher(phoneNumber).matches();
    }


}
