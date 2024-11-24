package Ecommerce.repository;

import Ecommerce.entities.Cart;
import Ecommerce.entities.AppUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
   

	Cart findByUser(AppUser user);

	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
	Optional<Cart> findByUserId(@Param("userId") Long userId);

}
