package br.com.fiap.javags.service;

import br.com.fiap.javags.dto.request.UserRequest;
import br.com.fiap.javags.dto.response.UserResponse;
import br.com.fiap.javags.entity.User;
import br.com.fiap.javags.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Construtor para injeção de dependência
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtém todos os usuários.
     */
    @Transactional(readOnly = true)
    public Collection<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtém um usuário por ID.
     */
    @Transactional(readOnly = true)
    public UserResponse findUserById(Long id) {
        User user = findUserEntityById(id);
        return toResponse(user);
    }

    /**
     * Cria um novo usuário.
     */
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        validateEmailUniqueness(userRequest.getEmail());
        User user = toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    /**
     * Atualiza um usuário existente.
     */
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User existingUser = findUserEntityById(id);

        if (!existingUser.getEmail().equals(userRequest.getEmail())) {
            validateEmailUniqueness(userRequest.getEmail());
        }

        existingUser.setNome(userRequest.getNome());
        existingUser.setEmail(userRequest.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return toResponse(updatedUser);
    }

    /**
     * Atualização customizada para suporte a mudanças parciais.
     */
    @Transactional
    public UserResponse updatePartialUser(Long id, UserRequest userRequest) {
        User existingUser = findUserEntityById(id);

        if (userRequest.getEmail() != null && !existingUser.getEmail().equals(userRequest.getEmail())) {
            validateEmailUniqueness(userRequest.getEmail());
            existingUser.setEmail(userRequest.getEmail());
        }

        if (userRequest.getNome() != null) {
            existingUser.setNome(userRequest.getNome());
        }

        User updatedUser = userRepository.save(existingUser);
        return toResponse(updatedUser);
    }

    /**
     * Deleta um usuário por ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserEntityById(id);
        userRepository.delete(user);
    }

    /**
     * Converte um UserRequest para uma entidade User.
     */
    private User toEntity(UserRequest userRequest) {
        return User.builder()
                .nome(userRequest.getNome())
                .email(userRequest.getEmail())
                .build();
    }

    /**
     * Converte uma entidade User para um UserResponse.
     */
    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .build();
    }

    /**
     * Valida se o email já está em uso.
     */
    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já está em uso");
        }
    }

    /**
     * Busca uma entidade User por ID ou lança uma exceção.
     */
    private User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    /**
     * Converte um UserResponse para UserRequest.
     */
    public UserRequest toRequest(UserResponse response) {
        return UserRequest.builder()
                .id(response.getId())
                .nome(response.getNome())
                .email(response.getEmail())
                .build();
    }
}
