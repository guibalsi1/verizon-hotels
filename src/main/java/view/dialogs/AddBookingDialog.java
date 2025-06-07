package view.dialogs;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.HospedeDAO;
import control.dao.QuartoDAO;
import control.dao.ReservaDAO;
import model.Hospede;
import model.Quarto;
import model.Reserva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddBookingDialog extends JDialog {
    private JComboBox<Hospede> hospedeComboBox;
    private JComboBox<Quarto> quartoComboBox;
    private JTextField checkInField;
    private JTextField checkOutField;
    private boolean reservaAdicionada = false;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DefaultListCellRenderer hospedeRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Hospede) {
                Hospede hospede = (Hospede) value;
                setText(hospede.getNome());
            }
            return this;
        }
    };

    private final DefaultListCellRenderer quartoRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Quarto) {
                Quarto quarto = (Quarto) value;
                setText("Quarto " + quarto.getNumero());  // Assumindo que existe um método getNumero()
            }
            return this;
        }
    };

    public AddBookingDialog(Frame owner) {
        super(owner, "Adicionar Nova Reserva", true);
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

        // Hóspede
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Hóspede:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        hospedeComboBox = new JComboBox<>();
        hospedeComboBox.setRenderer(hospedeRenderer);
        HospedeDAO hospedeDAO = new HospedeDAO();
        for (Hospede hospede : hospedeDAO.listar()) {
            hospedeComboBox.addItem(hospede);
        }
        mainPanel.add(hospedeComboBox, gbc);

        // Quarto
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Quarto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        quartoComboBox = new JComboBox<>();
        quartoComboBox.setRenderer(quartoRenderer);
        QuartoDAO quartoDAO = new QuartoDAO();
        for (Quarto quarto : quartoDAO.listar()) {
            quartoComboBox.addItem(quarto);

        }
        mainPanel.add(quartoComboBox, gbc);

        // Check-in
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Data Check-in (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        checkInField = new JTextField(10);
        mainPanel.add(checkInField, gbc);

        // Check-out
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Data Check-out (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        checkOutField = new JTextField(10);
        mainPanel.add(checkOutField, gbc);

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
                salvarReserva();
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
        if (hospedeComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um hóspede.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (quartoComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um quarto.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            LocalDate checkIn = LocalDate.parse(checkInField.getText(), dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutField.getText(), dateFormatter);

            if (checkOut.isBefore(checkIn)) {
                JOptionPane.showMessageDialog(this, "A data de check-out deve ser posterior à data de check-in.",
                        "Data inválida", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira as datas no formato correto (YYYY-MM-DD).",
                    "Formato de data inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void salvarReserva() {
        try {
            Hospede hospede = (Hospede) hospedeComboBox.getSelectedItem();
            Quarto quarto = (Quarto) quartoComboBox.getSelectedItem();
            String checkIn = checkInField.getText();
            String checkOut = checkOutField.getText();

            Reserva novaReserva = new Reserva(hospede, quarto, checkIn, checkOut);
            ReservaDAO reservaDAO = new ReservaDAO();
            reservaDAO.salvar(novaReserva);

            reservaAdicionada = true;
            JOptionPane.showMessageDialog(this, "Reserva adicionada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar reserva: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isReservaAdicionada() {
        return reservaAdicionada;
    }
}