package ca.ajaypatel.gatekeeper.management.repository;

import ca.ajaypatel.gatekeeper.management.entity.ManagementUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagementUserRepository extends JpaRepository<ManagementUser,Long> {

	
	Optional<ManagementUser> findByUsername(String username);
}
