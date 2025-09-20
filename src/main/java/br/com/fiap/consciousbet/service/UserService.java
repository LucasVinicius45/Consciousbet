package br.com.fiap.consciousbet.service;

import br.com.fiap.consciousbet.dto.UserCreateDTO;
import br.com.fiap.consciousbet.dto.UserResponseDTO;
import br.com.fiap.consciousbet.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    // Criar usuário
    UserResponseDTO create(UserCreateDTO dto);

    // Buscar todos (com paginação)
    Page<UserResponseDTO> findAll(Pageable pageable);

    // Buscar todos (lista simples)
    List<UserResponseDTO> findAll();

    // Buscar por ID
    UserResponseDTO findById(Long id);

    // Buscar por email
    UserResponseDTO findByEmail(String email);

    // Atualizar usuário
    UserResponseDTO update(Long id, UserUpdateDTO dto);

    // Atualizar parcialmente
    UserResponseDTO partialUpdate(Long id, UserUpdateDTO dto);

    // Deletar usuário
    void delete(Long id);

    // Verificar se existe por email
    boolean existsByEmail(String email);

    // Contar total de usuários
    long count();
}