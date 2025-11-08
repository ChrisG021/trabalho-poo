package com.sistema.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Classe Java Bean que representa um Aluno no sistema.
 * 
 * Requisitos implementados:
 * - Todos os atributos solicitados (matrícula, nome, idade, dataNascimento, telefone, CPF)
 * - Getters e Setters para todos os atributos
 * - Formatação de data no padrão dd/mm/yyyy
 * - Equals e HashCode baseados na matrícula (para evitar duplicatas)
 */
@Entity
@Table(name = "alunos")
public class Aluno implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Formato de data usado no sistema: dd/mm/yyyy
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    @Id
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;
    
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
    
    @Column(name = "idade", nullable = false)
    private int idade;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascimento", nullable = false)
    private Date dataNascimento;
    
    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;
    
    /**
     * CPF do aluno
     * Formato esperado: XXX.XXX.XXX-XX
     */
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;
    
    /**
     * Construtor padrão (necessário para JPA/Hibernate)
     */
    public Aluno() {
    }
    
    public Aluno(String matricula, String nome, int idade, Date dataNascimento, 
                 String telefone, String cpf) {
        this.matricula = matricula;
        this.nome = nome;
        this.idade = idade;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.cpf = cpf;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public String getMatricula() {
        return matricula;
    }
    

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public int getIdade() {
        return idade;
    }
    
    public void setIdade(int idade) {
        this.idade = idade;
    }
    

    public Date getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    public String getDataNascimentoFormatada() {
        if (dataNascimento == null) {
            return "";
        }
        return DATE_FORMAT.format(dataNascimento);
    }
    
    public void setDataNascimentoFromString(String dataString) throws ParseException {
        if (dataString != null && !dataString.trim().isEmpty()) {
            this.dataNascimento = DATE_FORMAT.parse(dataString);
        }
    }
    
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s",
                matricula,
                nome,
                getDataNascimentoFormatada(),
                telefone,
                cpf);
    }
    
    public static Aluno fromCSV(String csvLine) throws ParseException {
        String[] campos = csvLine.split(",");
        
        if (campos.length != 5) {
            throw new IllegalArgumentException("Linha CSV inválida. Esperado 5 campos, encontrado " + campos.length);
        }
        
        Aluno aluno = new Aluno();
        aluno.setMatricula(campos[0].trim());
        aluno.setNome(campos[1].trim());
        aluno.setDataNascimentoFromString(campos[2].trim());
        aluno.setTelefone(campos[3].trim());
        aluno.setCpf(campos[4].trim());
        
        // Calcula a idade aproximada baseada na data de nascimento
        aluno.calcularIdade();
        
        return aluno;
    }
    
    /**
     * Calcula e atualiza a idade do aluno baseado na data de nascimento
     */
    public void calcularIdade() {
        if (dataNascimento != null) {
            Date hoje = new Date();
            long diff = hoje.getTime() - dataNascimento.getTime();
            this.idade = (int) (diff / (1000L * 60 * 60 * 24 * 365));
        }
    }
    
    // ==================== EQUALS E HASHCODE ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return Objects.equals(matricula, aluno.matricula);
    }
    
    /**
     * Gera o código hash baseado na matrícula
     */
    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }
    
    /**
     * Representação textual do aluno para exibição
     */
    @Override
    public String toString() {
        return "Aluno{" +
                "matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                ", dataNascimento=" + getDataNascimentoFormatada() +
                ", telefone='" + telefone + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
