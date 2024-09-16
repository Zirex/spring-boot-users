package cl.nisum.models.dtos;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cl.nisum.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class UserDetailsDto extends User implements UserDetails {
    
    private String username;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsDto(String username, String password, GrantedAuthority authorities) {
		this.username = username;
		this.password = password;
		this.authorities = Arrays.asList(authorities);
	}

    public static UserDetailsDto build(User user) {
		GrantedAuthority authorities = user.getRole() != null ? new SimpleGrantedAuthority(user.getRole().getName().toString())
				: null;

		UserDetailsDto toReturn = new UserDetailsDto(user.getEmail(), user.getPassword(), authorities);
		toReturn.setId(user.getId());
        toReturn.setName(user.getName());
        toReturn.setActive(user.isActive());
        toReturn.setCreated(user.getCreated());
        toReturn.setModified(user.getModified());
        toReturn.setLastLogin(user.getLastLogin());
        toReturn.setPhones(user.getPhones());
        toReturn.setToken(user.getToken());

		return toReturn;
	}

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
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
