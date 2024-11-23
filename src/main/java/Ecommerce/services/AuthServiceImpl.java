package Ecommerce.services;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Ecommerce.dto.SignupRequest;
import Ecommerce.entities.AppUser;
import Ecommerce.repository.AppUserRepository;

@Service
public class AuthServiceImpl implements AuthService {
	
	private final AppUserRepository appuserRepository;
	
	private final PasswordEncoder passwordEncoder; 
	
	 
	 
	@Autowired
	 public AuthServiceImpl(AppUserRepository appuserRepository, PasswordEncoder passwordEncoder) {
	        this.appuserRepository = appuserRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
 
	@Override
	public boolean createUser(SignupRequest signupRequest) {
	    // Vérifiez si l'utilisateur existe déjà
	    if (appuserRepository.existsByEmail(signupRequest.getEmail())) {
	        return false;
	    }

	    // Créez un nouvel utilisateur
	    AppUser appuser = new AppUser();
	    appuser.setFirstName(signupRequest.getFirstName());
	    appuser.setLastName(signupRequest.getLastName());
	    appuser.setEmail(signupRequest.getEmail());
	    
	    // Encoder le mot de passe
	    String hashPassword = passwordEncoder.encode(signupRequest.getPassword());
	    appuser.setPassword(hashPassword);

	    // Définir le rôle par défaut
	    if (signupRequest.getRole() == null || signupRequest.getRole().isEmpty()) {
	        appuser.setRole("ROLE_USER"); // Par défaut, rôle USER
	    } else {
	        appuser.setRole(signupRequest.getRole());
	    }

	    // Sauvegardez l'utilisateur
	    appuserRepository.save(appuser);
	    return true;
	}

	
	

}
