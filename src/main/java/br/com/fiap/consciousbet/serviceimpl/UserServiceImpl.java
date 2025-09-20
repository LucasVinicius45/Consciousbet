package br.com.fiap.consciousbet.serviceimpl;

import br.com.fiap.consciousbet.dto.UserCreateDTO;
import br.com.fiap.consciousbet.dto.UserResponseDTO;
import br.com.fiap.consciousbet.dto.UserUpdateDTO;
import br.com.fiap.consciousbet.entity.User;
import br.com.fiap.consciousbet.mapper.UserMapper;
import br.com.fiap.consciousbet.repository.UserRepository;
import br.com.fiap.consciousbet.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        System.out.println("Creating user with email: " + dto.getEmail());

        // Verificar se email já existe
        if (existsByEmail(dto.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists: " + dto.getEmail());
        }

        // Converter DTO para entidade
        User user = UserMapper.toEntity(dto);

        // Salvar no banco
        User savedUser = userRepository.save(user);
        System.out.println("User created with ID: " + savedUser.getId());

        // Retornar DTO de resposta
        return UserMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        return UserMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        return UserMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        System.out.println("Updating user ID: " + id);

        // Buscar usuário existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        // Verificar se email está sendo alterado e já existe
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            if (existsByEmail(dto.getEmail())) {
                throw new DataIntegrityViolationException("Email already exists: " + dto.getEmail());
            }
        }

        // Atualizar todos os campos fornecidos
        UserMapper.updateEntity(existingUser, dto);

        // Salvar alterações
        User updatedUser = userRepository.save(existingUser);
        System.out.println("User updated successfully");

        return UserMapper.toResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO partialUpdate(Long id, UserUpdateDTO dto) {
        // Mesmo comportamento do update normal, pois já trata campos opcionais
        return update(id, dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        System.out.println("Deleting user ID: " + id);

        // Verificar se usuário existe
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        // Deletar usuário
        userRepository.deleteById(id);
        System.out.println("User deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }
}