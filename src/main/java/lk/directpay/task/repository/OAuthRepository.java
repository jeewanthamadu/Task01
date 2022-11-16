package lk.directpay.task.repository;

import lk.directpay.task.entity.AppUser;
import lk.directpay.task.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class OAuthRepository {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    public AppUser getUserDetails(String username) {
        Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
        List<AppUser> list = userRepository.findByUsername(username);
        if (list.size() > 0) {
            final AppUser appUser = list.get(0);
            for (String role :
                    appUser.getRoles()) {
                Optional<Role> roleObject = rolesRepository.findById(role);
                if (roleObject.isPresent()) {
                    for (String permission : roleObject.get().getPermissions()) {
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission);
                        grantedAuthoritiesList.add(grantedAuthority);
                    }
                }
            }//adding all permissions for each role

            appUser.setGrantedAuthoritiesList(grantedAuthoritiesList);
            return appUser;
        }
        return null;
    }
}
