package com.bank.privatebnk.repository;


import com.bank.privatebnk.domain.User;
import com.bank.privatebnk.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User,Long>{
    Optional<User> findByUserNameAndEnabledTrue(String username) throws ResourceNotFoundException;
//    @Query("SELECT new com.bank.sure.controller.dto.UserDTO(u.id,u) From User u")
//    Page<UserDTO> findUsersPage(Pageable pageable);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Boolean existsBySsn(String ssn);


    Optional<User> findOneWithAuthoritiesByUserName(String userName);





}
