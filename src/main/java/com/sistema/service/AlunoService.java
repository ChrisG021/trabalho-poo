package com.sistema.service;

import com.sistema.dao.AlunoDAO;
import com.sistema.dao.RemocaoAlunoDAO;
import com.sistema.exception.MatriculaDuplicadaException;
import com.sistema.model.Aluno;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe de serviço responsável por gerenciar todas as operações com Alunos.
 * 
 * Esta classe implementa todos os requisitos do trabalho:
 * - Requisito 1.b: Adicionar alunos em uma lista (Collections)
 * - Requisito 1.c: Obter aluno pela matrícula e quantidade de elementos
 * - Requisito 1.d: Remover aluno pela matrícula
 * - Requisito 1.e: Identificar aluno mais novo e mais velho
 * - Requisito 1.f: Inserir aluno em posição específica
 * - Requisito 2: Salvar registros em arquivo CSV
 */
public class AlunoService {
    
    // Lista em memória para armazenar os alunos
    private List<Aluno> listaAlunos;
    
    // DAO para operações de banco de dados
    private AlunoDAO alunoDAO;
    
    // Nome do arquivo CSV para persistência
    private static final String ARQUIVO_CSV = "ListagemAlunos.txt";
    
    /**
     * Construtor: inicializa a lista de alunos e o DAO
     */
    public AlunoService() {
        this.listaAlunos = new ArrayList<>();
        this.alunoDAO = new RemocaoAlunoDAO();
        
        // Carrega alunos do arquivo CSV ao iniciar
        carregarAlunosDoCSV();
    }
    
    // ==================== REQUISITO 1.B ====================
    public void adicionarAluno(Aluno aluno) throws MatriculaDuplicadaException {
        // Verifica se já existe um aluno com esta matrícula
        if (existeAlunoPorMatricula(aluno.getMatricula())) {
            // Lança exceção personalizada
            throw new MatriculaDuplicadaException(aluno.getMatricula(), true);
        }
        
        // Adiciona o aluno na lista
        listaAlunos.add(aluno);
        
        // Salva o aluno no banco de dados usando Hibernate
        try {
            alunoDAO.salvar(aluno);
        } catch (Exception e) {
            System.err.println("Erro ao salvar no banco de dados: " + e.getMessage());
        }
        
        // Salva a lista atualizada no arquivo CSV
        salvarAlunosNoCSV();
        
        System.out.println("Aluno adicionado com sucesso: " + aluno.getMatricula());
    }
    
    private boolean existeAlunoPorMatricula(String matricula) {
        for (Aluno aluno : listaAlunos) {
            if (aluno.getMatricula().equals(matricula)) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== REQUISITO 1.C ====================
    public Aluno obterAlunoPorMatricula(String matricula) {
        for (Aluno aluno : listaAlunos) {
            if (aluno.getMatricula().equals(matricula)) {
                // Imprime a quantidade de elementos da lista
                System.out.println("Aluno encontrado! Quantidade de alunos na lista: " + listaAlunos.size());
                return aluno;
            }
        }
        
        System.out.println("Aluno não encontrado. Quantidade de alunos na lista: " + listaAlunos.size());
        return null;
    }
    
    public int getQuantidadeAlunos() {
        return listaAlunos.size();
    }
    
    // ==================== REQUISITO 1.D ====================
    public boolean removerAlunoPorMatricula(String matricula) {
        // Busca o aluno pela matrícula
        Aluno alunoParaRemover = obterAlunoPorMatricula(matricula);
        
        if (alunoParaRemover == null) {
            System.out.println("Aluno não encontrado para remoção: " + matricula);
            return false;
        }
        
        // Usa o método removerAluno da interface AlunoDAO (REQUISITO 5)
        listaAlunos = alunoDAO.removerAluno(listaAlunos, alunoParaRemover);
        
        // Remove do banco de dados
        try {
            alunoDAO.remover(alunoParaRemover);
        } catch (Exception e) {
            System.err.println("Erro ao remover do banco de dados: " + e.getMessage());
        }
        
        // Atualiza o arquivo CSV
        salvarAlunosNoCSV();
        
        System.out.println("Aluno removido com sucesso: " + matricula);
        return true;
    }
    
    // ==================== REQUISITO 1.E ====================
    
    public Aluno[] identificarAlunosMaisNovoEMaisVelho() {
        if (listaAlunos.isEmpty()) {
            System.out.println("Lista vazia. Não há alunos para identificar.");
            return new Aluno[]{null, null};
        }
        
        Aluno maisNovo = listaAlunos.get(0);
        Aluno maisVelho = listaAlunos.get(0);
        
        // Percorre toda a lista comparando idades
        for (Aluno aluno : listaAlunos) {
            // Aluno mais novo = menor idade
            if (aluno.getIdade() < maisNovo.getIdade()) {
                maisNovo = aluno;
            }
            
            // Aluno mais velho = maior idade
            if (aluno.getIdade() > maisVelho.getIdade()) {
                maisVelho = aluno;
            }
        }
        
        System.out.println("\n=== IDENTIFICAÇÃO DE ALUNOS ===");
        System.out.println("Aluno MAIS NOVO:");
        System.out.println("  Nome: " + maisNovo.getNome());
        System.out.println("  Matrícula: " + maisNovo.getMatricula());
        System.out.println("  Idade: " + maisNovo.getIdade() + " anos");
        
        System.out.println("\nAluno MAIS VELHO:");
        System.out.println("  Nome: " + maisVelho.getNome());
        System.out.println("  Matrícula: " + maisVelho.getMatricula());
        System.out.println("  Idade: " + maisVelho.getIdade() + " anos");
        System.out.println("================================\n");
        
        return new Aluno[]{maisNovo, maisVelho};
    }
    
    // ==================== REQUISITO 1.F ====================

    public void inserirAlunoNaPosicao(Aluno aluno, int posicao) 
            throws MatriculaDuplicadaException, IndexOutOfBoundsException {
        
        // Verifica se já existe um aluno com esta matrícula
        if (existeAlunoPorMatricula(aluno.getMatricula())) {
            throw new MatriculaDuplicadaException(aluno.getMatricula(), true);
        }
        
        // Valida a posição
        if (posicao < 0 || posicao > listaAlunos.size()) {
            throw new IndexOutOfBoundsException(
                "Posição inválida: " + posicao + 
                ". A lista tem " + listaAlunos.size() + " elementos."
            );
        }
        
        // Insere o aluno na posição especificada
        listaAlunos.add(posicao, aluno);
        
        // Salva no banco de dados
        try {
            alunoDAO.salvar(aluno);
        } catch (Exception e) {
            System.err.println("Erro ao salvar no banco de dados: " + e.getMessage());
        }
        
        // Atualiza o arquivo CSV
        salvarAlunosNoCSV();
        
        System.out.println("Aluno inserido na posição " + posicao + ": " + aluno.getMatricula());
    }
    
    public void inserirAlunoNaTerceiraPosicao(Aluno aluno) 
            throws MatriculaDuplicadaException {
        try {
            inserirAlunoNaPosicao(aluno, 2); // Índice 2 = terceira posição
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Lista tem menos de 3 elementos. Adicionando no final.");
            adicionarAluno(aluno);
        }
    }
    
    // ==================== REQUISITO 2 ====================
    private void salvarAlunosNoCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            
            // Escreve cada aluno no arquivo CSV
            for (Aluno aluno : listaAlunos) {
                writer.write(aluno.toCSV());
                writer.newLine();
            }
            
            System.out.println("Alunos salvos no arquivo CSV: " + ARQUIVO_CSV);
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega os alunos do arquivo CSV para a lista em memória.
     * Este método é chamado ao iniciar o serviço.
     */
    private void carregarAlunosDoCSV() {
        File arquivo = new File(ARQUIVO_CSV);
        
        // Se o arquivo não existir, não há nada para carregar
        if (!arquivo.exists()) {
            System.out.println("Arquivo CSV não encontrado. Iniciando com lista vazia.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            
            String linha;
            int linhaNumero = 0;
            
            while ((linha = reader.readLine()) != null) {
                linhaNumero++;
                
                try {
                    // Cria um aluno a partir da linha CSV
                    Aluno aluno = Aluno.fromCSV(linha);
                    
                    // Adiciona na lista (sem verificar duplicatas aqui, pois é a carga inicial)
                    listaAlunos.add(aluno);
                    
                } catch (ParseException | IllegalArgumentException e) {
                    System.err.println("Erro ao processar linha " + linhaNumero + " do CSV: " + e.getMessage());
                }
            }
            
            System.out.println("Carregados " + listaAlunos.size() + " alunos do arquivo CSV");
            
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    public List<Aluno> listarTodosAlunos() {
        return new ArrayList<>(listaAlunos);
    }
    
    public void atualizarAluno(Aluno aluno) {
        // Busca o aluno na lista pela matrícula
        for (int i = 0; i < listaAlunos.size(); i++) {
            if (listaAlunos.get(i).getMatricula().equals(aluno.getMatricula())) {
                // Substitui o aluno antigo pelo atualizado
                listaAlunos.set(i, aluno);
                
                // Atualiza no banco de dados
                try {
                    alunoDAO.atualizar(aluno);
                } catch (Exception e) {
                    System.err.println("Erro ao atualizar no banco de dados: " + e.getMessage());
                }
                
                // Atualiza o arquivo CSV
                salvarAlunosNoCSV();
                
                System.out.println("Aluno atualizado com sucesso: " + aluno.getMatricula());
                return;
            }
        }
        
        System.out.println("Aluno não encontrado para atualização: " + aluno.getMatricula());
    }
    
    /**
     * Limpa todos os alunos da lista (use com cuidado!).
     */
    public void limparTodos() {
        listaAlunos.clear();
        salvarAlunosNoCSV();
        System.out.println("Todos os alunos foram removidos da lista.");
    }
    
    /**
     * Ordena a lista de alunos por nome.
     */
    public void ordenarPorNome() {
        listaAlunos.sort(Comparator.comparing(Aluno::getNome));
    }
    
    /**
     * Ordena a lista de alunos por matrícula.
     */
    public void ordenarPorMatricula() {
        listaAlunos.sort(Comparator.comparing(Aluno::getMatricula));
    }
    
    /**
     * Ordena a lista de alunos por idade.
     */
    public void ordenarPorIdade() {
        listaAlunos.sort(Comparator.comparingInt(Aluno::getIdade));
    }
}
