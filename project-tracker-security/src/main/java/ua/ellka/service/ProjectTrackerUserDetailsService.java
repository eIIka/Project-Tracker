package ua.ellka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.ellka.model.security.ProjectTrackerUserDetails;
import ua.ellka.model.user.User;
import ua.ellka.repo.UserRepo;

@RequiredArgsConstructor
public class ProjectTrackerUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new ProjectTrackerUserDetails(appUser);
    }
}
