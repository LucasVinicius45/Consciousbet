package br.com.fiap.consciousbet;

import br.com.fiap.consciousbet.entity.UserAuth;
import br.com.fiap.consciousbet.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ConsciousBetApplication {

    @Autowired
    private UserAuthRepository userAuthRepository;

    public static void main(String[] args) {
        SpringApplication.run(ConsciousBetApplication.class, args);
    }

    @PostConstruct
    public void initUser() {
        if (userAuthRepository.findByEmail("admin@email.com").isEmpty()) {
            UserAuth user = new UserAuth();
            user.setEmail("admin@email.com");
            user.setPassword("123456"); // simples, sem hash por enquanto
            userAuthRepository.save(user);
            System.out.println("Usuário admin criado com sucesso!");
        }
    }
}
