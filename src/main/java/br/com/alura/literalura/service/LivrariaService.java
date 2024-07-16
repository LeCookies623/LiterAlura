package br.com.alura.literalura.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.literalura.config.ClientRest;
import br.com.alura.literalura.config.DadosMapper;
import br.com.alura.literalura.dto.DadosLivroDTO;
import br.com.alura.literalura.dto.LivrosResponseApi;
import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;

public class LivrariaService {

    private Scanner sc = new Scanner(System.in);
    private ClientRest consumoApi = new ClientRest();
    private DadosMapper convertir = new DadosMapper();
    private static String API_BASE = "https://gutendex.com/books/?search=";
 
    private LivroRepository livroRepository;
    private AutorRepository autorRepository;
    public LivrariaService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void start(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    
                    |***************************************************|
                    |*****       Livraria LiterAlura              ******|
                    |***************************************************|
                    
                    1 - Buscar Livro pelo Titulo
                    2 - Listar Livros Registrados 
                    3 - Listar Autores Registrados 
                    4 - Listar Autores vivos em um determinado ano
                    5 - Listar Livros em um determinado idioma  
                   
                    
               
                    0 - Sair
                    
                    |***************************************************|
                    |*****        Selecione uma Opcao             ******|
                    |***************************************************|
                    """;

            try {
                System.out.println(menu);
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {

                System.out.println("|****************************************|");
                System.out.println("|  Por favor, insira un numero valido.  |");
                System.out.println("|****************************************|\n");
                sc.nextLine();
                continue;
            }
   
            switch (opcion){
                case 1:
                    buscarLivroNaAPI();
                    break;
                case 2:
                    livrosRegistrados();
                    break;
                case 3: 
                    buscarAutores();
                    break;
                case 4:
                    buscarAutoresPorAno();
                    break;
                case 5:
                    buscarLivrosPorIdioma();
                    break; 
                case 0:
                    opcion = 0;
                    System.out.println("|********************************|");
                    System.out.println("|    Fim da aplicação bye bye!   |");
                    System.out.println("|********************************|\n");
                    break;
                default:
                    System.out.println("|*********************|");
                    System.out.println("|  Opcao Incorrecta. |");
                    System.out.println("|*********************|\n");
                    System.out.println("Tente uma nova Opcao");
                    start();
                    break;
            }
        }
    }

    private Livro getDadosLivro(){
        System.out.println("Informe o nome do Livro: ");
        var nombreLivro = sc.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(API_BASE + nombreLivro.replace(" ", "%20"));
        
        LivrosResponseApi dados = convertir.converterDatosJsonAJava(json, LivrosResponseApi.class);

            if (dados != null && dados.getResultadoLivros() != null && !dados.getResultadoLivros().isEmpty()) {
                DadosLivroDTO primeiroLivro = dados.getResultadoLivros().get(0); 
                
                Livro livro =  new Livro(primeiroLivro);
                primeiroLivro.autor().forEach(autorDTO -> {
                	Autor autor = autorRepository.findFirstByNomeContainsIgnoreCase(autorDTO.nome()).orElse(null);
                	if (autor == null) {
                		autor = new Autor();
                		autor.setNome(autorDTO.nome());
                		autor.setNascimento(autorDTO.anoNascimento());
                		autor.setDataFalecimento(autorDTO.anoFalecimento());
                	}
                	
                	if (livro.getAutores()==null) {
            			livro.setAutores(new ArrayList<Autor>());
            		} 
            		livro.getAutores().add(autor);
                });;
                
                return livro;
            } else {
                System.out.println("Nenhum resultado encontrado.");
                return null;
            }
    }


    private void buscarLivroNaAPI() {
        Livro livro = getDadosLivro();

        if (livro == null){
            System.out.println("Livro nao encontrado. ");
            return;
        }
 
        try{
            Livro livroExists = livroRepository.findByTitulo(livro.getTitulo());
            if (livroExists != null){
                System.out.println("O livro ja existe no banco de dados!");
            }else {
                livroRepository.save(livro);
                System.out.println(livro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("Erro a gravaro livro!");
        }
    }

    @Transactional(readOnly = true)
    private void livrosRegistrados(){ 
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Não existem livros cadastrados.");
        } else {
            System.out.println("Livros cadastrados:");
            for (Livro livro : livros) {
                System.out.println(livro.toString());
            }
        }
    }
 
    private  void buscarAutores(){ 
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum livro encontrado \n");
        } else {
            System.out.println("Livros encontrados : \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                
                if (autoresUnicos.add(autor.getNome())){
                    System.out.println(autor.getNome()+'\n');
                }
            }
        }
    }

    private void  buscarLivrosPorIdioma(){
        System.out.println("Insira o Idioma para busca: \n");
        System.out.println("|***********************************|");
        System.out.println("|  Opcao - pt : Livros em portugues. |");
        System.out.println("|  Opcao - es : Livros em espanhol. |");
        System.out.println("|  Opcao - en : Livros em ingles.   |");
        System.out.println("|  Opcao - fr : Livros em fraces.   |");
        System.out.println("|***********************************|\n");

        var idioma = sc.nextLine();
        List<Livro> livrosPorIdioma = livroRepository.findByIdioma(idioma);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma selecionado.");
        } else {
            System.out.println("Livros no idioma selecionado:");
            for (Livro livro : livrosPorIdioma) {
                System.out.println(livro.toString());
            }
        }

    }

    private void buscarAutoresPorAno() {
 
        System.out.println("Informe o ano para consultar que autores estao vivos: \n");
        var anoNascimento = sc.nextInt();
        sc.nextLine();

        List<Autor> autoresVivos = autorRepository.findByNascimentoLessThanOrDataFalecimentoGreaterThanEqual(anoNascimento, anoNascimento);

        if (autoresVivos.isEmpty()) {
            System.out.println("Não foi encontrado autores que estavam vivos no ano de " + anoNascimento + ".");
        } else {
            System.out.println("O autores que estavan vivos no ano de " + anoNascimento + " são:");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor : autoresVivos) {
                if (autor.getNascimento() != null && autor.getDataFalecimento() != null) {
                    if (autor.getNascimento() <= anoNascimento && autor.getDataFalecimento() >= anoNascimento) {
                        if (autoresUnicos.add(autor.getNome())) {
                            System.out.println("Autor: " + autor.getNome());
                        }
                    }
                }
            }
        }
    }
 
}
