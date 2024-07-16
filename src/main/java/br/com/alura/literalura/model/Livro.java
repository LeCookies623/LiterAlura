package br.com.alura.literalura.model;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.alura.literalura.dto.DadosLivroDTO;
import br.com.alura.literalura.dto.MediaDTO;
import br.com.alura.literalura.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="id_livro")
    private Long livroId;
    
    @Column(name="titulo") 
    private String titulo;
    
    @Column(name="genero") 
    @Enumerated(EnumType.STRING)
    private Genero genero;
    
    @Column(name="idioma") 
    private String idioma;
    
    @Column(name="imagen") 
    private String imagen;
    
    @Column(name="quantidadeDownload") 
    private Long quantidadeDownload;
    

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( 
        name = "livro_autor",
        joinColumns = @JoinColumn(name = "id_livro"),
        inverseJoinColumns = @JoinColumn(name = "id_autor")
    )
    private List<Autor> autores;
 

    public Livro(DadosLivroDTO datosLivro) {
        this.livroId = datosLivro.livroId();
        this.titulo = datosLivro.titulo(); 
         
        this.genero =  generoModificado(datosLivro.genero());
        this.idioma = idiomaModificado(datosLivro.idioma());
        this.imagen = imagenModificada(datosLivro.imagen());
        this.quantidadeDownload = datosLivro.cantidadDescargas();
    }

    public Livro(Livro libro) {
    }

    private Genero generoModificado(List<String> generos) {
        if (generos == null || generos.isEmpty()) {
            return Genero.DESCONOCIDO;
        }
        Optional<String> firstGenero = generos.stream()
                .map(g -> {
                    int index = g.indexOf("--");
                    return index != -1 ? g.substring(index + 2).trim() : null;
                })
                .filter(Objects::nonNull)
                .findFirst();
        return firstGenero.map(Genero::fromString).orElse(Genero.DESCONOCIDO);
    }

    private String idiomaModificado(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }

    private String imagenModificada(MediaDTO media) {
        if (media == null || media.imagen().isEmpty()) {
            return "Sin imagen";
        }
        return media.imagen();
    } 
 
 

    @Override
    public String toString() {
        return
                "  \nid=" + id +
                "  \nLivro id=" + livroId +
                ", \ntitulo='" + titulo + '\'' +
                ", \nauthors=" + ((getAutores() != null  && ! getAutores().isEmpty()) ? nomeAutores() : "N/A")+
                ", \ngenero=" + genero +
                ", \nidioma=" + idioma +
                ", \nimagen=" + imagen +
                ", \ncantidadDescargas=" + quantidadeDownload;
    }
    
    public String nomeAutores() {
    	StringBuilder sb = new StringBuilder();
    	getAutores().stream().forEach(autor -> sb.append(autor.getNome()+ " "));
    	
    	
    	return sb.toString();
    }

 
}
