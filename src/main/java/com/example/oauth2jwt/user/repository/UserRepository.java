package com.example.oauth2jwt.user.repository;

import com.example.oauth2jwt.user.entity.SocialType;
import com.example.oauth2jwt.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByRefreshToken(String refreshToken);

  Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
