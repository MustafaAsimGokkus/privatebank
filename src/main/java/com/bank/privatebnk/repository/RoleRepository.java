package com.bank.privatebnk.repository;

import com.bank.privatebnk.domain.Role;
import com.bank.privatebnk.domain.enumeration.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole name);


}