package br.com.alura.literalura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.literalura.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

	Livro findByTitulo(String titulo);
 

	List<Livro> findByIdioma(String idioma);
   
 

}
