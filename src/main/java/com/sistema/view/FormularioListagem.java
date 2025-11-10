package com.sistema.view;

import com.sistema.model.Aluno;
import com.sistema.service.AlunoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class FormularioListagem extends JFrame {
    
    // Serviço para gerenciar alunos
    private AlunoService alunoService;
    
    // Componentes da interface
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotal;

    public FormularioListagem(AlunoService alunoService) {
        this.alunoService = alunoService;
        
        // Configura a janela
        setTitle("Listagem de Alunos");
        setSize(900, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Cria os componentes
        criarComponentes();
        
        // Carrega os dados dos alunos
        carregarDados();
        
        // Torna a janela visível
        setVisible(true);
    }
    

    private void criarComponentes() {
        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel do título
        JPanel painelTitulo = criarPainelTitulo();
        
        // Painel da tabela
        JPanel painelTabela = criarPainelTabela();
        
        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        
        // Painel de informações (rodapé)
        JPanel painelInfo = criarPainelInfo();
        
        // Adiciona os painéis
        painelPrincipal.add(painelTitulo, BorderLayout.NORTH);
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.EAST);
        painelPrincipal.add(painelInfo, BorderLayout.SOUTH);
        
        add(painelPrincipal);
    }

    private JPanel criarPainelTitulo() {
        JPanel painel = new JPanel();
        painel.setBackground(new Color(70, 130, 180)); // SteelBlue
        
        JLabel lblTitulo = new JLabel("LISTAGEM DE ALUNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        
        painel.add(lblTitulo);
        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Define as colunas da tabela
        String[] colunas = {
            "Matrícula", "Nome", "Idade", "Data Nasc.", "Telefone", "CPF"
        };
        
        // (não editável)
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        // Cria a tabela
        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaAlunos.setRowHeight(25);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaAlunos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaAlunos.getTableHeader().setBackground(new Color(70, 130, 180));
        tabelaAlunos.getTableHeader().setForeground(Color.WHITE);
        
        // Define larguras das colunas
        tabelaAlunos.getColumnModel().getColumn(0).setPreferredWidth(100); // Matrícula
        tabelaAlunos.getColumnModel().getColumn(1).setPreferredWidth(250); // Nome
        tabelaAlunos.getColumnModel().getColumn(2).setPreferredWidth(60);  // Idade
        tabelaAlunos.getColumnModel().getColumn(3).setPreferredWidth(100); // Data Nasc.
        tabelaAlunos.getColumnModel().getColumn(4).setPreferredWidth(130); // Telefone
        tabelaAlunos.getColumnModel().getColumn(5).setPreferredWidth(130); // CPF
        
        // Adiciona a tabela em um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Alunos Cadastrados"));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        return painel;
    }
    
    /**
     * Cria o painel com os botões de ação.
     */
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // Botão Atualizar
        JButton btnAtualizar = criarBotao("Atualizar", new Color(46, 139, 87));
        btnAtualizar.addActionListener(e -> carregarDados());
        
        // Botão Ordenar por Nome
        JButton btnOrdenarNome = criarBotao("Ordenar por Nome", new Color(70, 130, 180));
        btnOrdenarNome.addActionListener(e -> ordenarPorNome());
        
        // Botão Ordenar por Matrícula
        JButton btnOrdenarMatricula = criarBotao("Ordenar por Matrícula", new Color(70, 130, 180));
        btnOrdenarMatricula.addActionListener(e -> ordenarPorMatricula());
        
        // Botão Ordenar por Idade
        JButton btnOrdenarIdade = criarBotao("Ordenar por Idade", new Color(70, 130, 180));
        btnOrdenarIdade.addActionListener(e -> ordenarPorIdade());
        
        // Botão Ver Detalhes
        JButton btnDetalhes = criarBotao("Ver Detalhes", new Color(255, 140, 0));
        btnDetalhes.addActionListener(e -> verDetalhes());
        
        // Botão Exportar CSV
        JButton btnExportar = criarBotao("Exportar CSV", new Color(128, 128, 128));
        btnExportar.addActionListener(e -> exportarCSV());
        
        // Botão Fechar
        JButton btnFechar = criarBotao("Fechar", new Color(220, 20, 60));
        btnFechar.addActionListener(e -> dispose());
        
        // Adiciona os botões com espaçamento
        painel.add(btnAtualizar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(btnOrdenarNome);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(btnOrdenarMatricula);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(btnOrdenarIdade);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(btnDetalhes);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(btnExportar);
        painel.add(Box.createRigidArea(new Dimension(0, 20)));
        painel.add(btnFechar);
        
        return painel;
    }
    
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBackground(cor);
        botao.setFocusPainted(false);
        botao.setMaximumSize(new Dimension(150, 35));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        return botao;
    }
    
    /**
     * Cria o painel de informações (rodapé).
     */
    private JPanel criarPainelInfo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBorder(BorderFactory.createEtchedBorder());
        
        lblTotal = new JLabel("Total de alunos: 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        
        painel.add(lblTotal);
        return painel;
    }
    
    private void carregarDados() {
        // Limpa a tabela
        modeloTabela.setRowCount(0);
        
        // Obtém a lista de alunos
        List<Aluno> alunos = alunoService.listarTodosAlunos();
        
        // Adiciona cada aluno na tabela
        for (Aluno aluno : alunos) {
            Object[] linha = {
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getDataNascimentoFormatada(),
                aluno.getTelefone(),
                aluno.getCpf()
            };
            modeloTabela.addRow(linha);
        }
        
        // Atualiza o total
        lblTotal.setText("Total de alunos: " + alunos.size());
    }

    private void ordenarPorNome() {
        alunoService.ordenarPorNome();
        carregarDados();
        JOptionPane.showMessageDialog(
            this,
            "Alunos ordenados por nome!",
            "Ordenação",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void ordenarPorMatricula() {
        alunoService.ordenarPorMatricula();
        carregarDados();
        JOptionPane.showMessageDialog(
            this,
            "Alunos ordenados por matrícula!",
            "Ordenação",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void ordenarPorIdade() {
        alunoService.ordenarPorIdade();
        carregarDados();
        JOptionPane.showMessageDialog(
            this,
            "Alunos ordenados por idade!",
            "Ordenação",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    

    private void verDetalhes() {
        int linhaSelecionada = tabelaAlunos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecione um aluno na tabela!",
                "Nenhum Aluno Selecionado",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Obtém a matrícula do aluno selecionado
        String matricula = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        
        // Busca o aluno completo
        Aluno aluno = alunoService.obterAlunoPorMatricula(matricula);
        
        if (aluno != null) {
            String mensagem = String.format(
                "DETALHES DO ALUNO\n\n" +
                "Matrícula: %s\n" +
                "Nome: %s\n" +
                "Idade: %d anos\n" +
                "Data de Nascimento: %s\n" +
                "Telefone: %s\n" +
                "CPF: %s",
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getDataNascimentoFormatada(),
                aluno.getTelefone(),
                aluno.getCpf()
            );
            
            JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Detalhes do Aluno",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    private void exportarCSV() {
        String mensagem = 
            "Os alunos já estão sendo salvos automaticamente\n" +
            "no arquivo CSV: ListagemAlunos.txt\n\n" +
            "Este arquivo é atualizado toda vez que você:\n" +
            "  Adiciona um novo aluno\n" +
            "  Remove um aluno\n" +
            "  Atualiza dados de um aluno\n\n" +
            "Formato do arquivo:\n" +
            "matricula,nome,dataNascimento,telefone,cpf\n\n" +
            "Exemplo:\n" +
            "123,Paulo Roberto Marinho,35/01/10/2015,(86)3232-2525,554.759.013-00";
        
        JOptionPane.showMessageDialog(
            this,
            mensagem,
            "Exportação para CSV",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
