package br.com.alura.literalura.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LivrosResponseApi {

    @JsonAlias("results")
    List<DadosLivroDTO> resultadoLivros;
}
