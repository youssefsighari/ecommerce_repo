package Ecommerce.services.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Ecommerce.entities.AppUser;
import Ecommerce.repository.AppUserRepository;

@Service
public class UserServiceImpl implements UserDetailsService { 
	
	private final AppUserRepository appuserRepository;
	
	

	public UserServiceImpl(AppUserRepository appuserRepository) {
		this.appuserRepository = appuserRepository;
		
	} 
 
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    System.out.println("Attempting to load user by email: " + email);
	    AppUser appuser = appuserRepository.findByEmail(email)
	            .orElseThrow(() -> {
	                System.out.println("appuser not found with email: " + email);
	                return new UsernameNotFoundException("appuser not found with email: " + email);
	            });
	    System.out.println("appuser found: " + appuser.getEmail());
	    
	    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority(appuser.getRole())); // Ex: ROLE_ADMIN ou ROLE_USER
	    
	    return new User(appuser.getEmail(), appuser.getPassword(), authorities);
	}


	public AppUser getUserByEmail(String email) {
	    return appuserRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("appuser not found with email: " + email));
	}

	
 

}
 