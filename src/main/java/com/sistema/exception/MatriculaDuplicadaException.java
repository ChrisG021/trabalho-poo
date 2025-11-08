package com.sistema.exception;


public class MatriculaDuplicadaException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Matrícula que causou o problema
     */
    private String matricula;
    
    /**
     * Construtor padrão
     */
    public MatriculaDuplicadaException() {
        super("Matrícula duplicada no sistema");
    }
    
    /**
     * Construtor com mensagem personalizada
     */
    public MatriculaDuplicadaException(String mensagem) {
        super(mensagem);
    }
    
    /**
     * Construtor com matrícula duplicada
     */
    public MatriculaDuplicadaException(String matricula, boolean incluirMatricula) {
        super("Já existe um aluno cadastrado com a matrícula: " + matricula);
        this.matricula = matricula;
    }
    
    /**
     * Construtor completo com mensagem e causa
     */
    public MatriculaDuplicadaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
    
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
