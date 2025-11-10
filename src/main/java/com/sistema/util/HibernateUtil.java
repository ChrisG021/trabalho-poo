package com.sistema.util;

import com.sistema.model.Aluno;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    
    /**
     * Instância única da SessionFactory (Singleton)
     */
    private static SessionFactory sessionFactory;
    
    /**
     * Construtor privado para prevenir instanciação
     */
    private HibernateUtil() {
    }
    
    /**
     * Obtém a SessionFactory do Hibernate.
     * 
     * Se a SessionFactory ainda não foi criada, este método a cria e configura.
     * Implementa o padrão Singleton thread-safe.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    sessionFactory = buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }
    
    private static SessionFactory buildSessionFactory() {
        try {
            // Cria a configuração do Hibernate
            Configuration configuration = new Configuration();
            
            // ===== CONFIGURAÇÕES DE CONEXÃO COM O BANCO DE DADOS =====
            
            // HSQLDB (banco de dados em memória )
            configuration.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
            configuration.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:./data/alunosdb");
            configuration.setProperty("hibernate.connection.username", "SA");
            configuration.setProperty("hibernate.connection.password", "");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            
            // ===== CONFIGURAÇÕES DO HIBERNATE =====
            
            // Pool de conexões
            configuration.setProperty("hibernate.c3p0.min_size", "5");
            configuration.setProperty("hibernate.c3p0.max_size", "20");
            configuration.setProperty("hibernate.c3p0.timeout", "300");
            configuration.setProperty("hibernate.c3p0.max_statements", "50");
            configuration.setProperty("hibernate.c3p0.idle_test_period", "3000");
            
            // Configuração de DDL (criação automática de tabelas)
            // update: atualiza o schema do banco sem apagar dados
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            
            // Mostrar SQL no console (útil para debug)
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            
            // Usar comentários SQL
            configuration.setProperty("hibernate.use_sql_comments", "true");
            
            // Timezone
            configuration.setProperty("hibernate.jdbc.time_zone", "America/Fortaleza");
            
            
            
            // Registra a classe Aluno como entidade mapeada
            configuration.addAnnotatedClass(Aluno.class);
            
            
            // Cria o ServiceRegistry
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            
            // Constrói e retorna a SessionFactory
            SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);
            
            System.out.println("SessionFactory do Hibernate criada com sucesso!");
            
            return factory;
            
        } catch (Exception e) {
            System.err.println("Erro ao criar SessionFactory do Hibernate!");
            System.err.println("Verifique as configurações do banco de dados e as dependências do projeto.");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("SessionFactory do Hibernate encerrada.");
        }
    }
    
    public static boolean isSessionFactoryOpen() {
        return sessionFactory != null && !sessionFactory.isClosed();
    }
}
