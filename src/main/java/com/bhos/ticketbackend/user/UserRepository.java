package com.bhos.ticketbackend.user;

import com.bhos.ticketbackend.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "select * from User u", nativeQuery = true)
    List<UserDTO> getAllUser();
}
