package com.sistema.view;

import com.sistema.exception.MatriculaDuplicadaException;
import com.sistema.model.Aluno;
import com.sistema.service.AlunoService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormularioPrincipal extends JFrame {
    
    // Serviço para gerenciar alunos
    private AlunoService alunoService;
    
    // Componentes do formulário
    private JTextField txtMatricula;
    private JTextField txtNome;
    private JSpinner spinnerIdade;
    private JFormattedTextField txtDataNascimento;
    private JFormattedTextField txtTelefone;
    private JFormattedTextField txtCPF;
    
    // Formato de data
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    public FormularioPrincipal() {
        
        alunoService = new AlunoService();
        
        setTitle("Sistema de Gerenciamento de Alunos");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setResizable(false);
        criarMenu();
        criarComponentes();
        setVisible(true);
    }
    

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (opcao == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuArquivo.add(itemSair);
        
        // Menu Alunos
        JMenu menuAlunos = new JMenu("Alunos");
        
        JMenuItem itemListar = new JMenuItem("Listar Todos");
        itemListar.addActionListener(e -> abrirListagemAlunos());
        
        JMenuItem itemBuscar = new JMenuItem("Buscar por Matrícula");
        itemBuscar.addActionListener(e -> buscarAluno());
        
        JMenuItem itemRemover = new JMenuItem("Remover Aluno");
        itemRemover.addActionListener(e -> removerAluno());
        
        JMenuItem itemMaisNovoVelho = new JMenuItem("Identificar Mais Novo/Velho");
        itemMaisNovoVelho.addActionListener(e -> identificarMaisNovoVelho());
        
        JMenuItem itemInserirPosicao = new JMenuItem("Inserir em Posição");
        itemInserirPosicao.addActionListener(e -> inserirNaPosicao());
        
        menuAlunos.add(itemListar);
        menuAlunos.add(itemBuscar);
        menuAlunos.addSeparator();
        menuAlunos.add(itemRemover);
        menuAlunos.addSeparator();
        menuAlunos.add(itemMaisNovoVelho);
        menuAlunos.add(itemInserirPosicao);
        
        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        
        // Adiciona os menus na barra
        menuBar.add(menuArquivo);
        menuBar.add(menuAlunos);
        menuBar.add(menuAjuda);
        
        setJMenuBar(menuBar);
    }
    
    private void criarComponentes() {
        // Painel principal com BorderLayout
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel do título
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(70, 130, 180)); // SteelBlue
        JLabel lblTitulo = new JLabel("CADASTRO DE ALUNOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        painelTitulo.add(lblTitulo);
        
        // Painel do formulário
        JPanel painelFormulario = criarPainelFormulario();
        
        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        
        // Adiciona os painéis ao painel principal
        painelPrincipal.add(painelTitulo, BorderLayout.NORTH);
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        // Adiciona o painel principal à janela
        add(painelPrincipal);
    }
    
    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "Dados do Aluno",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int linha = 0;
        
        // Campo Matrícula
        adicionarCampo(painel, gbc, linha++, "Matrícula:", 
            txtMatricula = new JTextField(20));
        
        // Campo Nome
        adicionarCampo(painel, gbc, linha++, "Nome Completo:", 
            txtNome = new JTextField(20));
        
        // Campo Idade
        SpinnerNumberModel modeloIdade = new SpinnerNumberModel(18, 0, 120, 1);
        spinnerIdade = new JSpinner(modeloIdade);
        adicionarCampo(painel, gbc, linha++, "Idade:", spinnerIdade);
        
        // Campo Data de Nascimento
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtDataNascimento = new JFormattedTextField(mascaraData);
        } catch (ParseException e) {
            txtDataNascimento = new JFormattedTextField();
        }
        adicionarCampo(painel, gbc, linha++, "Data Nascimento:", txtDataNascimento);
        
        // Campo Telefone
        try {
            MaskFormatter mascaraTelefone = new MaskFormatter("(##)#####-####");
            mascaraTelefone.setPlaceholderCharacter('_');
            txtTelefone = new JFormattedTextField(mascaraTelefone);
        } catch (ParseException e) {
            txtTelefone = new JFormattedTextField();
        }
        adicionarCampo(painel, gbc, linha++, "Telefone:", txtTelefone);
        
        // Campo CPF
        try {
            MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
            mascaraCPF.setPlaceholderCharacter('_');
            txtCPF = new JFormattedTextField(mascaraCPF);
        } catch (ParseException e) {
            txtCPF = new JFormattedTextField();
        }
        adicionarCampo(painel, gbc, linha++, "CPF:", txtCPF);
        
        return painel;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc, 
                                int linha, String rotulo, JComponent campo) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(label, gbc);
        
        // Campo
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campo.setPreferredSize(new Dimension(250, 30));
        painel.add(campo, gbc);
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar Aluno");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(46, 139, 87)); // SeaGreen
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(150, 40));
        btnSalvar.addActionListener(e -> salvarAluno());
        
        // Botão Limpar
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpar.setBackground(new Color(255, 165, 0)); // Orange
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setPreferredSize(new Dimension(150, 40));
        btnLimpar.addActionListener(e -> limparCampos());
        
        // Botão Listar Alunos (REQUISITO 3)
        JButton btnListar = new JButton("Listar Alunos");
        btnListar.setFont(new Font("Arial", Font.BOLD, 14));
        btnListar.setBackground(new Color(70, 130, 180)); // SteelBlue
        btnListar.setForeground(Color.WHITE);
        btnListar.setFocusPainted(false);
        btnListar.setPreferredSize(new Dimension(150, 40));
        btnListar.addActionListener(e -> abrirListagemAlunos());
        
        painel.add(btnSalvar);
        painel.add(btnLimpar);
        painel.add(btnListar);
        
        return painel;
    }
    
    private void salvarAluno() {
        try {
            // Valida os campos
            if (!validarCampos()) {
                return;
            }
            
            // Cria o objeto Aluno
            Aluno aluno = new Aluno();
            aluno.setMatricula(txtMatricula.getText().trim());
            aluno.setNome(txtNome.getText().trim());
            aluno.setIdade((Integer) spinnerIdade.getValue());
            
            // Converte a data de nascimento
            Date dataNascimento = DATE_FORMAT.parse(txtDataNascimento.getText());
            aluno.setDataNascimento(dataNascimento);
            
            aluno.setTelefone(txtTelefone.getText().trim());
            aluno.setCpf(txtCPF.getText().trim());
            
            // Tenta adicionar o aluno
            alunoService.adicionarAluno(aluno);
            
            // Mostra mensagem de sucesso
            JOptionPane.showMessageDialog(
                this,
                "Aluno cadastrado com sucesso!\n" +
                "Total de alunos: " + alunoService.getQuantidadeAlunos(),
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Limpa os campos
            limparCampos();
            
        } catch (MatriculaDuplicadaException e) {
            // Trata exceção de matrícula duplicada (REQUISITO 1.b)
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Matrícula Duplicada",
                JOptionPane.ERROR_MESSAGE
            );
            txtMatricula.requestFocus();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                this,
                "Data de nascimento inválida!\nUse o formato: dd/mm/aaaa",
                "Erro de Validação",
                JOptionPane.ERROR_MESSAGE
            );
            txtDataNascimento.requestFocus();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar aluno:\n" + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private boolean validarCampos() {
        // Valida matrícula
        if (txtMatricula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a matrícula!", 
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtMatricula.requestFocus();
            return false;
        }
        
        // Valida nome
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome!", 
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        
        // Valida data de nascimento
        if (txtDataNascimento.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Informe a data de nascimento completa!", 
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtDataNascimento.requestFocus();
            return false;
        }
        
        // Valida telefone
        if (txtTelefone.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Informe o telefone completo!", 
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtTelefone.requestFocus();
            return false;
        }
        
        // Valida CPF
        if (txtCPF.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Informe o CPF completo!", 
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtCPF.requestFocus();
            return false;
        }
        
        return true;
    }
    

    private void limparCampos() {
        txtMatricula.setText("");
        txtNome.setText("");
        spinnerIdade.setValue(18);
        txtDataNascimento.setText("");
        txtTelefone.setText("");
        txtCPF.setText("");
        txtMatricula.requestFocus();
    }
    
 
    private void abrirListagemAlunos() {
        new FormularioListagem(alunoService);
    }
    
    /**
     * Busca um aluno pela matrícula.
     * IMPLEMENTA REQUISITO 1.c: Obter aluno pela matrícula
     */
    private void buscarAluno() {
        String matricula = JOptionPane.showInputDialog(
            this,
            "Informe a matrícula do aluno:",
            "Buscar Aluno",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (matricula != null && !matricula.trim().isEmpty()) {
            Aluno aluno = alunoService.obterAlunoPorMatricula(matricula.trim());
            
            if (aluno != null) {
                String mensagem = String.format(
                    "ALUNO ENCONTRADO!\n\n" +
                    "Matrícula: %s\n" +
                    "Nome: %s\n" +
                    "Idade: %d anos\n" +
                    "Data Nascimento: %s\n" +
                    "Telefone: %s\n" +
                    "CPF: %s\n\n" +
                    "Total de alunos na lista: %d",
                    aluno.getMatricula(),
                    aluno.getNome(),
                    aluno.getIdade(),
                    aluno.getDataNascimentoFormatada(),
                    aluno.getTelefone(),
                    aluno.getCpf(),
                    alunoService.getQuantidadeAlunos()
                );
                
                JOptionPane.showMessageDialog(
                    this,
                    mensagem,
                    "Aluno Encontrado",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Aluno não encontrado!\n\n" +
                    "Total de alunos na lista: " + alunoService.getQuantidadeAlunos(),
                    "Não Encontrado",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private void removerAluno() {
        String matricula = JOptionPane.showInputDialog(
            this,
            "Informe a matrícula do aluno a ser removido:",
            "Remover Aluno",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (matricula != null && !matricula.trim().isEmpty()) {
            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja remover o aluno com matrícula " + matricula + "?",
                "Confirmação de Remoção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean removido = alunoService.removerAlunoPorMatricula(matricula.trim());
                
                if (removido) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Aluno removido com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Aluno não encontrado!",
                        "Não Encontrado",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        }
    }
    

    private void identificarMaisNovoVelho() {
        if (alunoService.getQuantidadeAlunos() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não há alunos cadastrados!",
                "Lista Vazia",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        Aluno[] resultado = alunoService.identificarAlunosMaisNovoEMaisVelho();
        Aluno maisNovo = resultado[0];
        Aluno maisVelho = resultado[1];
        
        String mensagem = String.format(
            "ALUNO MAIS NOVO:\n" +
            "Nome: %s\n" +
            "Matrícula: %s\n" +
            "Idade: %d anos\n\n" +
            "ALUNO MAIS VELHO:\n" +
            "Nome: %s\n" +
            "Matrícula: %s\n" +
            "Idade: %d anos",
            maisNovo.getNome(),
            maisNovo.getMatricula(),
            maisNovo.getIdade(),
            maisVelho.getNome(),
            maisVelho.getMatricula(),
            maisVelho.getIdade()
        );
        
        JOptionPane.showMessageDialog(
            this,
            mensagem,
            "Identificação de Alunos",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    

    private void inserirNaPosicao() {
        String posicaoStr = JOptionPane.showInputDialog(
            this,
            "Informe a posição onde deseja inserir o novo aluno\n" +
            "(0 = primeira posição, 1 = segunda, etc):\n\n" +
            "Total de alunos na lista: " + alunoService.getQuantidadeAlunos(),
            "Inserir em Posição",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (posicaoStr != null && !posicaoStr.trim().isEmpty()) {
            try {
                int posicao = Integer.parseInt(posicaoStr.trim());
                
                // Confirma se deseja preencher um novo aluno
                int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Agora preencha os dados do aluno no formulário principal\n" +
                    "e ele será inserido na posição " + posicao + ".\n\n" +
                    "Deseja continuar?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Funcionalidade: Preencha o formulário principal e clique em 'Salvar'\n" +
                        "para adicionar o aluno. Para inserir em posição específica, use o menu\n" +
                        "'Alunos > Inserir em Posição' após preencher os dados.",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inválida! Informe um número.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void mostrarSobre() {
        String mensagem = 
            "SISTEMA DE GERENCIAMENTO DE ALUNOS\n\n" +
            "RUMBORAA";
        
        JOptionPane.showMessageDialog(
            this,
            mensagem,
            "Sobre o Sistema",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        // Define o Look and Feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new FormularioPrincipal());
    }
}
