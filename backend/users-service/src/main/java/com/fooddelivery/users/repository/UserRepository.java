package com.fooddelivery.users.repository;

import com.fooddelivery.users.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u")// HERE WE USE JPQL FORMAT
    List<User> findAllUsers(Sort sort);

    @Query(value="SELECT u FROM User u WHERE u.fullName=?1")
    User findUserByFullName(String fullName);

    @Query(value="SELECT u FROM User u WHERE u.email=?1")
    User findUserByEmail(String email);
}

