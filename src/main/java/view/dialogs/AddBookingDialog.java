package view.dialogs;

import com.formdev.flatlaf.FlatClientProperties;
import com.toedter.calendar.JCalendar;
import control.dao.HospedeDAO; // Mantido para o novo método
import control.dao.QuartoDAO;   // Mantido para o novo método
import model.Hospede;
import model.Quarto;
import model.Reserva;
import model.exceptions.ReservaInvalidaException;
import control.facade.ReservaFacade; // Usado para salvar

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;

public class AddBookingDialog extends JDialog {
    // --- Componentes da UI ---
    private JComboBox<Hospede> hospedeComboBox;
    private JComboBox<Quarto> quartoComboBox;
    private JCalendar checkInField;
    private JCalendar checkOutField;
    private JPanel detailsPanel; // NOVO: Painel para a segunda etapa
    private JPanel buttonPanelStep2; // NOVO: Painel de botões para a segunda etapa
    private JButton verificarButton; // NOVO: Botão para a primeira etapa

    private boolean reservaAdicionada = false;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // DAOs para buscar listas de disponíveis
    private final QuartoDAO quartoDAO = new QuartoDAO();
    private final HospedeDAO hospedeDAO = new HospedeDAO();

    // Renderers para os ComboBoxes (sem alteração)
    private final DefaultListCellRenderer hospedeRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Hospede hospede) setText(hospede.getNome());
            return this;
        }
    };
    private final DefaultListCellRenderer quartoRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Quarto quarto) setText("Quarto " + quarto.getNumero());
            return this;
        }
    };

    public AddBookingDialog(Frame owner) {
        super(owner, "Adicionar Nova Reserva", true);
        initComponents();
        pack(); // Ajusta o tamanho inicial
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- ETAPA 1: SELEÇÃO DE DATAS ---
        JPanel datePanel = new JPanel(new GridBagLayout());
        datePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; datePanel.add(new JLabel("Data Check-in:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; checkInField = new JCalendar(); datePanel.add(checkInField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; datePanel.add(new JLabel("Data Check-out:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; checkOutField = new JCalendar(); datePanel.add(checkOutField, gbc);

        JPanel buttonPanelStep1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        verificarButton = new JButton("Verificar Disponibilidade");
        verificarButton.addActionListener(e -> onVerificarDisponibilidade());
        buttonPanelStep1.add(verificarButton);

        // --- ETAPA 2: DETALHES DA RESERVA (INICIALMENTE OCULTO) ---
        detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        detailsPanel.setVisible(false); // Começa oculto

        gbc.gridx = 0; gbc.gridy = 0; detailsPanel.add(new JLabel("Hóspede Disponível:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        hospedeComboBox = new JComboBox<>();
        hospedeComboBox.setRenderer(hospedeRenderer);
        detailsPanel.add(hospedeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; detailsPanel.add(new JLabel("Quarto Disponível:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        quartoComboBox = new JComboBox<>();
        quartoComboBox.setRenderer(quartoRenderer);
        detailsPanel.add(quartoComboBox, gbc);

        buttonPanelStep2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salvarButton = new JButton("Salvar Reserva");
        salvarButton.putClientProperty(FlatClientProperties.STYLE, "background: #4CAF50; foreground: #FFFFFF; arc: 8");
        salvarButton.addActionListener(e -> salvarReserva());
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.putClientProperty(FlatClientProperties.STYLE, "background: #f44336; foreground: #FFFFFF; arc: 8");
        cancelarButton.addActionListener(e -> dispose());
        buttonPanelStep2.add(salvarButton);
        buttonPanelStep2.add(cancelarButton);
        buttonPanelStep2.setVisible(false); // Começa oculto

        // Adiciona os painéis ao diálogo
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(datePanel);
        centerPanel.add(detailsPanel);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(buttonPanelStep1);
        southPanel.add(buttonPanelStep2);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    // --- LÓGICA DE CONTROLE ---

    private boolean validarDatas() {
        try {
            LocalDate checkIn = LocalDate.parse(coverterData(checkInField.getCalendar()), dateFormatter);
            LocalDate checkOut = LocalDate.parse(coverterData(checkOutField.getCalendar()), dateFormatter);
            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                JOptionPane.showMessageDialog(this, "A data de check-out deve ser posterior à data de check-in.", "Data Inválida", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onVerificarDisponibilidade() {
        if (!validarDatas()) return;

        String checkIn = coverterData(checkInField.getCalendar());
        String checkOut = coverterData(checkOutField.getCalendar());

        // Busca as listas filtradas usando os novos métodos do DAO
        List<Quarto> quartosDisponiveis = quartoDAO.listarQuartosDisponiveis(checkIn, checkOut);
        List<Hospede> hospedesDisponiveis = hospedeDAO.listarHospedesDisponiveis(checkIn, checkOut);

        if (quartosDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum quarto disponível para o período selecionado.", "Sem Disponibilidade", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Popula os ComboBoxes com os dados filtrados
        hospedeComboBox.setModel(new DefaultComboBoxModel<>(hospedesDisponiveis.toArray(new Hospede[0])));
        quartoComboBox.setModel(new DefaultComboBoxModel<>(quartosDisponiveis.toArray(new Quarto[0])));

        // Exibe a segunda etapa da UI
        detailsPanel.setVisible(true);
        buttonPanelStep2.setVisible(true);
        verificarButton.setVisible(false); // Oculta o botão da primeira etapa
        pack(); // Reajusta o tamanho do diálogo
    }

    private void salvarReserva() {
        if (hospedeComboBox.getSelectedItem() == null || quartoComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um hóspede e um quarto.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Hospede hospede = (Hospede) hospedeComboBox.getSelectedItem();
            Quarto quarto = (Quarto) quartoComboBox.getSelectedItem();
            String checkIn = coverterData(checkInField.getCalendar());
            String checkOut = coverterData(checkOutField.getCalendar());

            Reserva novaReserva = new Reserva(hospede, quarto, checkIn, checkOut);
            novaReserva.adicionarParticipante(hospede);

            ReservaFacade reservaFacade = new ReservaFacade();
            reservaFacade.salvar(novaReserva);

            reservaAdicionada = true;
            JOptionPane.showMessageDialog(this, "Reserva adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (ReservaInvalidaException e) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar a reserva:\n" + e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar reserva: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String coverterData(Calendar dataIntrodusida) {
        int ano = dataIntrodusida.get(Calendar.YEAR);
        int mes = dataIntrodusida.get(Calendar.MONTH) + 1;
        int dia = dataIntrodusida.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", ano, mes, dia);
    }

    public boolean isReservaAdicionada() {
        return reservaAdicionada;
    }
}