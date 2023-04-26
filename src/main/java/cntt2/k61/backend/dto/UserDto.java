package cntt2.k61.backend.dto;

import cntt2.k61.backend.domain.UserRole;

public class UserDto {
    private String userName;
    private String userPassword;
    private UserRole role;

    public UserDto() {
    }

    public UserDto(String userName, String userPassword, UserRole role) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
