package com.socialmedia.livevibe.repository;

import com.socialmedia.livevibe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
//    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
}
