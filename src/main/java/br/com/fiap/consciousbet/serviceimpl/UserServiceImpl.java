package br.com.fiap.consciousbet.serviceimpl;

import br.com.fiap.consciousbet.dto.UserDTO;
import br.com.fiap.consciousbet.entity.User;
import br.com.fiap.consciousbet.mapper.UserMapper;
import br.com.fiap.consciousbet.repository.UserRepository;
import br.com.fiap.consciousbet.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDTO create(UserDTO dto) {
        User user = UserMapper.toEntity(dto);
        return UserMapper.toDTO(repo.save(user));
    }

    @Override
    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        return repo.findById(id).map(UserMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User user = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return UserMapper.toDTO(repo.save(user));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
