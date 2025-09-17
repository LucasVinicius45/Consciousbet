package br.com.fiap.consciousbet;

import br.com.fiap.consciousbet.entity.UserAuth;
import br.com.fiap.consciousbet.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class ConsciousBetApplication {

    @Autowired
    private UserAuthRepository userAuthRepository;

    public static void main(String[] args) {
        SpringApplication.run(ConsciousBetApplication.class, args);
    }

    // Usar @EventListener em vez de @PostConstruct para garantir que tudo esteja inicializado
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initUser() {
        try {
            if (userAuthRepository.findByEmail("admin@email.com").isEmpty()) {
                UserAuth user = new UserAuth();
                user.setEmail("admin@email.com");
                user.setPassword("123456"); // simples, sem hash por enquanto
                userAuthRepository.save(user);
                System.out.println("✅ Usuário admin criado com sucesso!");
            } else {
                System.out.println("ℹ️ Usuário admin já existe no banco de dados.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar usuário admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}