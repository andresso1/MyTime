package com.MyTime.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PrimaryUser implements UserDetails {
    private final String nombre;
    private final String nombreUsuario;
    private final String email;
    private final String password;

    private final Integer orgId;

    private final Integer userId;

    public Integer getOrgId() {
        return orgId;
    }

    public Integer getUserId() {
        return userId;
    }

    private final Collection<? extends GrantedAuthority> authorities;

    public PrimaryUser(String nombre, String nombreUsuario, String email, String password, Collection<? extends GrantedAuthority> authorities, Integer orgid, Integer userId) {
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.orgId = orgid;
        this.userId = userId;
    }

    public static PrimaryUser build(User usuario){
        List<GrantedAuthority> authorities =
                usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
                .getRolName().name())).collect(Collectors.toList());
        return new PrimaryUser(usuario.getName(), usuario.getUserName(), usuario.getEmail(), usuario.getPassword(), authorities, usuario.getCompany().getCompanyId(), usuario.getUserId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

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
