package ua.ellka.model.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.ellka.model.user.User;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ProjectTrackerUserDetails implements UserDetails {
    private final User user;

    public Long getId(){
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getNickname(){
        return user.getNickname();
    }
}
