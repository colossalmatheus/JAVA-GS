package br.com.fiap.javags.service;

import br.com.fiap.javags.dto.request.EletroRequest;
import br.com.fiap.javags.dto.response.EletroResponse;
import br.com.fiap.javags.entity.Eletro;
import br.com.fiap.javags.entity.User;
import br.com.fiap.javags.repository.EletroRepository;
import br.com.fiap.javags.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class EletroService {

    private final EletroRepository eletroRepository;
    private final UserRepository userRepository;

    public EletroService(EletroRepository eletroRepository, UserRepository userRepository) {
        this.eletroRepository = eletroRepository;
        this.userRepository = userRepository;
    }

    /**
     * Método para obter todos os eletrodomésticos
     */
    public Collection<EletroResponse> findAll() {
        return eletroRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Método para buscar os eletrodomésticos de um usuário específico
     */
    public Collection<EletroResponse> findByUserId(Long userId) {
        User user = findUserById(userId); // Verifica se o usuário existe
        return eletroRepository.findByUser(user).stream() // Busca eletrodomésticos pelo usuário
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Método para buscar um eletrodoméstico por ID e retornar um EletroResponse
     */
    public EletroResponse findByIdResponse(Long id) {
        Eletro eletro = findById(id);
        return toResponse(eletro);
    }

    /**
     * Método para buscar um eletrodoméstico por ID
     */
    public Eletro findById(Long id) {
        return eletroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Eletrodoméstico não encontrado"));
    }

    /**
     * Método para salvar um novo eletrodoméstico a partir de um EletroRequest
     */
    public EletroResponse save(EletroRequest dto) {
        User user = findUserById(dto.getUserId()); // Correção: getUserId()
        Eletro eletro = toEntity(dto);
        eletro.setUser(user);
        eletro = eletroRepository.save(eletro);
        return toResponse(eletro);
    }

    /**
     * Método para atualizar um eletrodoméstico existente
     */
    public EletroResponse update(Long id, EletroRequest dto) {
        Eletro eletroExistente = findById(id);
        User user = findUserById(dto.getUserId()); // Correção: getUserId()

        eletroExistente.setNome(dto.getNome());
        eletroExistente.setTipo(dto.getTipo());
        eletroExistente.setPotencia(dto.getPotencia());
        eletroExistente.setUso(dto.getUso());
        eletroExistente.setUser(user);

        Eletro eletroAtualizado = eletroRepository.save(eletroExistente);
        return toResponse(eletroAtualizado);
    }

    /**
     * Método para deletar um eletrodoméstico
     */
    public void delete(Long id) {
        Eletro eletro = findById(id);
        eletroRepository.delete(eletro);
    }

    /**
     * Método para buscar um usuário por ID
     */
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    /**
     * Método para converter um EletroRequest em Eletro (entidade)
     */
    public Eletro toEntity(EletroRequest dto) {
        return Eletro.builder()
                .nome(dto.getNome())
                .tipo(dto.getTipo())
                .potencia(dto.getPotencia())
                .uso(dto.getUso())
                .build();
    }

    /**
     * Método para converter um Eletro (entidade) em EletroResponse
     */
    public EletroResponse toResponse(Eletro eletro) {
        return EletroResponse.builder()
                .id(eletro.getId())
                .nome(eletro.getNome())
                .tipo(eletro.getTipo())
                .potencia(eletro.getPotencia())
                .uso(eletro.getUso())
                .userId(eletro.getUser() != null ? eletro.getUser().getId() : null) // Correção: userId
                .build();
    }

    /**
     * Método para converter um EletroResponse em EletroRequest
     */
    public EletroRequest toRequest(EletroResponse response) {
        return EletroRequest.builder()
                .id(response.getId())
                .nome(response.getNome())
                .tipo(response.getTipo())
                .potencia(response.getPotencia())
                .uso(response.getUso())
                .userId(response.getUserId()) // Correção: userId
                .build();
    }
}
