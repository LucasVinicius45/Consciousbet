package br.com.fiap.consciousbet.service;

import br.com.fiap.consciousbet.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(UserDTO dto);
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);
}
