package app.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import app.entities.Token;
import jakarta.transaction.Transactional;

public interface ITokenRepository extends ListCrudRepository<Token, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE Token t SET t.valid = false WHERE t.authorizationCode.id = :codeId")
	public void invalidateToken(@Param("codeId") int codeId);
	
}
