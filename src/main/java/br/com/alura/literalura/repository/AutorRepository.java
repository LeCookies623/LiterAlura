package br.com.alura.literalura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.literalura.model.Autor; 

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

	public List<Autor> findByNascimentoLessThanOrDataFalecimentoGreaterThanEqual(int ano1, int ano2);

	public Optional<Autor> findFirstByNomeContainsIgnoreCase(String nome);


}
