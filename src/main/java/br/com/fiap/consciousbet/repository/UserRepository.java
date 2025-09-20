package br.com.fiap.consciousbet.repository;

import br.com.fiap.consciousbet.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar por email
    Optional<User> findByEmail(String email);

    // Verificar se existe por email
    boolean existsByEmail(String email);

    // Buscar por email ignorando case
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    // Buscar usuários por idade mínima
    @Query("SELECT u FROM User u WHERE u.age >= :minAge")
    Page<User> findByAgeGreaterThanEqual(@Param("minAge") Integer minAge, Pageable pageable);

    // Buscar por nome contendo (busca parcial)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // Contar usuários por idade mínima
    @Query("SELECT COUNT(u) FROM User u WHERE u.age >= :minAge")
    long countByAgeGreaterThanEqual(@Param("minAge") Integer minAge);
}