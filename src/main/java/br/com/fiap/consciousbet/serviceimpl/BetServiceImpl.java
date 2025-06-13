package br.com.fiap.consciousbet.serviceimpl;

import br.com.fiap.consciousbet.dto.BetDTO;
import br.com.fiap.consciousbet.entity.Bet;
import br.com.fiap.consciousbet.entity.User;
import br.com.fiap.consciousbet.repository.BetRepository;
import br.com.fiap.consciousbet.repository.UserRepository;
import br.com.fiap.consciousbet.service.BetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetServiceImpl implements BetService {

    private final BetRepository betRepo;
    private final UserRepository userRepo;

    public BetServiceImpl(BetRepository betRepo, UserRepository userRepo) {
        this.betRepo = betRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Bet create(BetDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Bet bet = new Bet(dto.getAmount(), dto.getType(), user);
        return betRepo.save(bet);
    }

    @Override
    public List<Bet> findAll() {
        return betRepo.findAll();
    }

    @Override
    public List<Bet> findByUserId(Long userId) {
        return betRepo.findByUserId(userId);
    }
}
