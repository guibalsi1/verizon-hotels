package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.HospedeDAO;
import model.Hospede;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;

public class AdicionarHospedeDialog extends JDialog {
    private JTextField nomeField;
    private JFormattedTextField cpfField;
    private JFormattedTextField telefoneField;
    private boolean hospedeAdicionado = false;

    public AdicionarHospedeDialog(Frame owner) {
        super(owner, "Adicionar Novo Hóspede", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel principal com padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        nomeField = new JTextField(20);
        mainPanel.add(nomeField, gbc);

        // CPF
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

        // Telefone
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        try {
            javax.swing.text.MaskFormatter telMask = new javax.swing.text.MaskFormatter("(##) #####-####");
            telefoneField = new JFormattedTextField(telMask);
        } catch (ParseException e) {
            telefoneField = new JFormattedTextField();
        }
        mainPanel.add(telefoneField, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salvarButton = new JButton("Salvar");
        JButton cancelarButton = new JButton("Cancelar");

        // Estilização dos botões
        salvarButton.putClientProperty(FlatClientProperties.STYLE,
                "background: #4CAF50; foreground: #FFFFFF; arc: 8");
        cancelarButton.putClientProperty(FlatClientProperties.STYLE,
                "background: #f44336; foreground: #FFFFFF; arc: 8");

        // Ação do botão Salvar
        salvarButton.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                salvarHospede();
            }
        });

        // Ação do botão Cancelar
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

        String telefone = telefoneField.getText().replaceAll("[^0-9]", "");
        if (telefone.length() < 10) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o telefone corretamente.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void salvarHospede() {
        try {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().replaceAll("[^0-9]", "");
            String telefone = telefoneField.getText().replaceAll("[^0-9]", "");

            Hospede novoHospede = new Hospede(cpf, nome, telefone);
            HospedeDAO hospedeDAO = new HospedeDAO();
            hospedeDAO.salvar(novoHospede);

            hospedeAdicionado = true;
            JOptionPane.showMessageDialog(this, "Hóspede adicionado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar hóspede: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isHospedeAdicionado() {
        return hospedeAdicionado;
    }
}