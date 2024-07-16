package br.com.alura.literalura.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MediaDTO(
    @JsonAlias("image/jpeg") String imagen
){}
