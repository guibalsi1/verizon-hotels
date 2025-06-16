package view.dialogs;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.FuncionarioDAO;
import model.Funcionario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;

public class AddWorkerDialog extends JDialog {
    private JTextField nomeField;
    private JFormattedTextField cpfField;
    private JTextField cargoField;
    private boolean funcionarioAdicionado = false;

    public AddWorkerDialog(Frame owner) {
        super(owner, "Adicionar Novo Funcionário", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        nomeField = new JTextField(20);
        mainPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        try {
            javax.swing.text.MaskFormatter cpfMask = new javax.swing.text.MaskFormatter("###.###.###-##");
            cpfField = new JFormattedTextField(cpfMask);
        } catch (ParseException e) {
            cpfField = new JFormattedTextField();
        }
        mainPanel.add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Cargo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        cargoField = new JTextField(20); // Inicialização do campo
        mainPanel.add(cargoField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salvarButton = new JButton("Salvar");
        JButton cancelarButton = new JButton("Cancelar");

        salvarButton.putClientProperty(FlatClientProperties.STYLE,
                "background: #4CAF50; foreground: #FFFFFF; arc: 8");
        cancelarButton.putClientProperty(FlatClientProperties.STYLE,
                "background: #f44336; foreground: #FFFFFF; arc: 8");

        salvarButton.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                salvarFuncionario();
            }
        });

        cancelarButton.addActionListener(e -> dispose());

        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o nome.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String cpf = cpfField.getText().replaceAll("[^0-9]", "");
        if (cpf.length() != 11) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o CPF corretamente.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cargoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o cargo.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void salvarFuncionario() {
        try {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().replaceAll("[^0-9]", "");
            String cargo = cargoField.getText().trim();

            Funcionario novoFuncionario = new Funcionario(cpf, nome, cargo);
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            funcionarioDAO.salvar(novoFuncionario);

            funcionarioAdicionado = true;
            JOptionPane.showMessageDialog(this, "Funcionário adicionado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar Funcionário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isWorkerAdded() {
        return funcionarioAdicionado;
    }
}