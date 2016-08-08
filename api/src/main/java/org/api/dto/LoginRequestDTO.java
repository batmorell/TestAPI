package org.api.dto;

import java.io.Serializable;

public class LoginRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public LoginRequestDTO() {
        super();
    }

    public LoginRequestDTO(String email, String password, boolean isAdminLogin) {
        super();
        this.email = email;
        this.password = password;
    }

    public String email;
    public String password;
}

