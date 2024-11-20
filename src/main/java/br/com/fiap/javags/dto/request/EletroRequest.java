package br.com.fiap.javags.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EletroRequest {

        private Long id;

        @NotBlank(message = "O nome é obrigatório")
        private String nome;

        @NotBlank(message = "O tipo é obrigatório")
        private String tipo;

        @NotNull(message = "A potência é obrigatória")
        @Min(value = 0, message = "A potência deve ser positiva")
        private Double potencia;

        @NotNull(message = "O uso é obrigatório")
        @Min(value = 0, message = "O uso deve ser positivo")
        private Double uso;

        @NotNull(message = "O usuário é obrigatório")
        private Long userId; // Campo correto: userId


}