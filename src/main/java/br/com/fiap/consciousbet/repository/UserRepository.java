package br.com.fiap.consciousbet.repository;

import br.com.fiap.consciousbet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
