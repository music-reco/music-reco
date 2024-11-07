package com.e106.reco.domain.artist.user.dto;

import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.entity.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails{
    private final Long seq;
    private final String email;
    private final String nickname;
    private final String password;
    private final String role;

    private final String year;

    private final Gender gender;
    private final Region region;
    private final Position position;
    private final Genre genre;

    private final List<Long> crews;

    //roll 반환하는 것
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }

//    public UserRole getRole() { return role; }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { return email;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
