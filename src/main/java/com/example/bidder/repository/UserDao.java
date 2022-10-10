package com.example.bidder.repository;
import com.example.bidder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,String> {
    Optional<User> findById(int user_id);
}
