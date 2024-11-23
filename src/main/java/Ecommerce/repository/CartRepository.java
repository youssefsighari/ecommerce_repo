package Ecommerce.repository;

import Ecommerce.entities.Cart;
import Ecommerce.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
   

	Cart findByUser(AppUser user);

	Cart findByUserId(Long userId);
}
