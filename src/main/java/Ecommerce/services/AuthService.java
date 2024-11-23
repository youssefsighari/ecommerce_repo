package Ecommerce.services;



import Ecommerce.dto.SignupRequest;
import Ecommerce.entities.AppUser; 

public interface AuthService {

	boolean createUser(SignupRequest signupRequest);

	

}
 