package com.bank.privatebnk.repository;

import com.bank.privatebnk.domain.Account;
import com.bank.privatebnk.domain.Recipient;
import com.bank.privatebnk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {
	
	Boolean existsByUserAndAccount(User user, Account account);
	
	
	@Query("SELECT r from Recipient r  where r.user.id=:userId and r.account.id=:accountId")
	Optional<Recipient> findRecipientByUserAndAccountId(@Param ("userId") Long userId, @Param("accountId") Long accountId);
	

}
