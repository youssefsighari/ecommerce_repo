package Ecommerce.repository;



import java.util.Optional;  

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ecommerce.entities.AppUser;  

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long > {

	boolean existsByEmail(String email);

	Optional<AppUser> findByEmail(String email);

} 
 