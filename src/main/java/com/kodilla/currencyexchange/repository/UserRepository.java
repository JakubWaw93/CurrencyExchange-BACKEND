package com.kodilla.currencyexchange.repository;

import com.kodilla.currencyexchange.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default void delete(User entity) {
        entity.setActive(false);
        save(entity);
    }

    Optional<User> findByIdAndActiveTrue(Long id);

    Optional<User> findByEmailAddressAndActiveTrue(String email);

    List<User> findAllByActiveTrue();

    Optional<User> findByLoginAndPasswordAndActiveTrue(String login, String password);

    boolean existsByEmailAddressAndActiveTrue(String email);

    boolean existsByLoginAndActiveTrue(String login);

}
