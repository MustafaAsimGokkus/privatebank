package com.bank.privatebnk.repository;

import com.bank.privatebnk.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository <Role,Integer> {
}
