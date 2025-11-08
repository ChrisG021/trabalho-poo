package com.sistema.dao;

import com.sistema.model.Aluno;
import java.util.List;


public interface AlunoDAO {

    List<Aluno> removerAluno(List<Aluno> alunos, Aluno a);
    
    void salvar(Aluno aluno);
 
    void atualizar(Aluno aluno);
 
    void remover(Aluno aluno);
    
    Aluno buscarPorMatricula(String matricula);

    List<Aluno> listarTodos();

    boolean existePorMatricula(String matricula);
}
