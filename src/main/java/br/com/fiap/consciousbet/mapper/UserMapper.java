package br.com.fiap.consciousbet.mapper;

import br.com.fiap.consciousbet.dto.UserCreateDTO;
import br.com.fiap.consciousbet.dto.UserResponseDTO;
import br.com.fiap.consciousbet.dto.UserUpdateDTO;
import br.com.fiap.consciousbet.entity.User;

public class UserMapper {

    // DTO de criação para entidade
    public static User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return user;
    }

    // Entidade para DTO de resposta
    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // Atualizar entidade com DTO de atualização (apenas campos não nulos)
    public static void updateEntity(User user, UserUpdateDTO dto) {
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            user.setName(dto.getName().trim());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            user.setEmail(dto.getEmail().trim().toLowerCase());
        }
        if (dto.getAge() != null) {
            user.setAge(dto.getAge());
        }
    }
}