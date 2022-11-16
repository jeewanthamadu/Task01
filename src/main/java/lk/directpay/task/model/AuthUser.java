package lk.directpay.task.model;


import lk.directpay.task.entity.AppUser;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    public static final String TYPE_USER = "user";

    private static final long serialVersionUID = 1L;

    public AuthUser(AppUser appUser) {
        super(appUser.getUsername(), appUser.getPassword(), appUser.getGrantedAuthoritiesList());
    }
}
