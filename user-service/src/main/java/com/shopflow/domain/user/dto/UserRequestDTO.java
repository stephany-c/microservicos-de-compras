package com.shopflow.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
    
    @NotBlank(message = "O nome não pode estar vazio")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    String email,

    @NotBlank
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String password
) {}
