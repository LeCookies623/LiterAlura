package br.com.alura.literalura.model;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "autores")
@Data
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "dt_nascimento")
    private Integer nascimento;

    @Column(name = "dt_falecimento")
    private Integer dataFalecimento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Livro> livros;
   

    public Autor() {
    }
  

    @Override
    public String toString() {
        return
                "nombre='" + nome + '\'' +
                ", Nascimento=" + nascimento +
                ", dataFalecimento=" + dataFalecimento;
    }
}
