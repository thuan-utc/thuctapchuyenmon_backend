package cntt2.k61.backend.controller;

import cntt2.k61.backend.domain.User;
import cntt2.k61.backend.domain.UserRole;
import cntt2.k61.backend.dto.SignupDto;
import cntt2.k61.backend.dto.UserDto;
import cntt2.k61.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AccountController {
    private Logger log = LoggerFactory.getLogger(AccountController.class);
    private UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        log.info("checking login for userDto {}", userDto);
        Optional<User> userOpt = userService.checkLogin(userDto);
        if (userOpt.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("userRole", userOpt.get().getRole());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupDto signupDto) {
        log.info("process signup for newUser {}", signupDto);
        return userService.processSignup(signupDto);
    }
}
