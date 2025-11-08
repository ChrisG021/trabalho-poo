package com.sistema.dao;

import com.sistema.model.Aluno;
import com.sistema.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;


public class RemocaoAlunoDAO implements AlunoDAO {

    @Override
    public List<Aluno> removerAluno(List<Aluno> alunos, Aluno a) {
        if (alunos == null) {
            return new ArrayList<>();
        }
        
        if (a == null) {
            return alunos;
        }
        
        // Remove o aluno da lista
        // O método remove usa equals(), que está implementado na classe Aluno
        // comparando pela matrícula
        alunos.remove(a);
        
        return alunos;
    }
    
    /**
     * Salva um novo aluno no banco de dados usando Hibernate.
     * 
     * IMPLEMENTA REQUISITO 4: Usar Hibernate para salvar aluno
     * 
     * @param aluno Aluno a ser salvo
     */
    @Override
    public void salvar(Aluno aluno) {
        Transaction transaction = null;
        Session session = null;
        
        try {
            // Abre uma nova sessão do Hibernate
            session = HibernateUtil.getSessionFactory().openSession();
            
            // Inicia uma transação
            transaction = session.beginTransaction();
            
            // Salva o aluno no banco de dados
            session.save(aluno);
            
            // Confirma a transação
            transaction.commit();
            
            System.out.println("Aluno salvo com sucesso no banco de dados: " + aluno.getMatricula());
            
        } catch (Exception e) {
            // Em caso de erro, desfaz a transação
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Erro ao salvar aluno: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar aluno no banco de dados", e);
            
        } finally {
            // Fecha a sessão
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void atualizar(Aluno aluno) {
        Transaction transaction = null;
        Session session = null;
        
        try {
            // Abre uma nova sessão do Hibernate
            session = HibernateUtil.getSessionFactory().openSession();
            
            // Inicia uma transação
            transaction = session.beginTransaction();
            
            // Atualiza o aluno no banco de dados
            session.update(aluno);
            
            // Confirma a transação
            transaction.commit();
            
            System.out.println("Aluno atualizado com sucesso no banco de dados: " + aluno.getMatricula());
            
        } catch (Exception e) {
            // Em caso de erro, desfaz a transação
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Erro ao atualizar aluno: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar aluno no banco de dados", e);
            
        } finally {
            // Fecha a sessão
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public void remover(Aluno aluno) {
        Transaction transaction = null;
        Session session = null;
        
        try {
            // Abre uma nova sessão do Hibernate
            session = HibernateUtil.getSessionFactory().openSession();
            
            // Inicia uma transação
            transaction = session.beginTransaction();
            
            // Remove o aluno do banco de dados
            session.delete(aluno);
            
            // Confirma a transação
            transaction.commit();
            
            System.out.println("Aluno removido com sucesso do banco de dados: " + aluno.getMatricula());
            
        } catch (Exception e) {
            // Em caso de erro, desfaz a transação
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Erro ao remover aluno: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao remover aluno do banco de dados", e);
            
        } finally {
            // Fecha a sessão
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Aluno buscarPorMatricula(String matricula) {
        Session session = null;
        Aluno aluno = null;
        
        try {
            // Abre uma nova sessão do Hibernate
            session = HibernateUtil.getSessionFactory().openSession();
            
            // Busca o aluno pela matrícula (chave primária)
            aluno = session.get(Aluno.class, matricula);
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar aluno por matrícula: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
            // Fecha a sessão
            if (session != null) {
                session.close();
            }
        }
        
        return aluno;
    }
    

    @Override
    public List<Aluno> listarTodos() {
        Session session = null;
        List<Aluno> alunos = new ArrayList<>();
        
        try {
            // Abre uma nova sessão do Hibernate
            session = HibernateUtil.getSessionFactory().openSession();
            
            // Cria uma query HQL para buscar todos os alunos
            Query<Aluno> query = session.createQuery("FROM Aluno", Aluno.class);
            
            // Executa a query e obtém a lista de alunos
            alunos = query.list();
            
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os alunos: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
            // Fecha a sessão
            if (session != null) {
                session.close();
            }
        }
        
        return alunos;
    }
    
    @Override
    public boolean existePorMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }
}
