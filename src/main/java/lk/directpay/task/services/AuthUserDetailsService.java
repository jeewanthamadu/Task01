package lk.directpay.task.services;



import lk.directpay.task.entity.AppUser;
import lk.directpay.task.model.AuthUser;
import lk.directpay.task.repository.OAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    OAuthRepository oAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser;
        try {
            appUser = oAuthRepository.getUserDetails(username);
            if(appUser == null)
                throw new UsernameNotFoundException("User " + username + " was not found in the database");
            
            AuthUser authUser = new AuthUser(appUser);
            return authUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
    }
}
