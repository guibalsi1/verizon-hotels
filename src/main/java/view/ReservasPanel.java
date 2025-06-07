package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.ReservaDAO;
import control.dao.QuartoDAO;
import control.dao.HospedeDAO;
import model.Reserva;
import view.dialogs.AddBookingDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservasPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Padding da área de conteúdo

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Reservas");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "title-label");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(8, 0));
        searchBarPanel.setOpaque(false);
        JTextField searchField = new JTextField();
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Busca por numero da reserva...");

        JButton searchButton;
        ImageIcon searchIconPng = null;
        try {
            java.net.URL imgUrl = HospedesPanel.class.getClassLoader().getResource(ICON_BUSCAR);
            if (imgUrl != null) {
                searchIconPng = new ImageIcon(imgUrl);
                Image scaledImg = searchIconPng.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                searchButton = new JButton(new ImageIcon(scaledImg));
            } else {
                searchButton = new JButton("🔍");
                System.err.println("Ícone de busca não encontrado: " + ICON_BUSCAR);
            }
        } catch (Exception e) {
            searchButton = new JButton("🔍");
            System.err.println("Erro ao carregar ícone de busca: " + e.getMessage());
        }
        searchButton.setName("searchButton");
        searchButton.setToolTipText("Buscar");

        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);
        topAreaPanel.add(searchBarPanel, BorderLayout.CENTER);

        contentPanel.add(topAreaPanel, BorderLayout.NORTH);

        // Painel de cards com ScrollPane
        // Substitua a criação do cardsPanel por:
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 25, 0));

        // Adiciona ScrollPane
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Adiciona o scrollPane ao contentPanel (adicione esta linha)
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        HospedeDAO hospedeDAO = new HospedeDAO();
        QuartoDAO quartoDAO = new QuartoDAO();
        ReservaDAO reservaDAO = new ReservaDAO();
        List<Reserva> reservas = reservaDAO.listarTodas(hospedeDAO, quartoDAO);

        // Cria cards para cada reserva
        for (Reserva reserva: reservas) {
            JPanel card = createReservaCard(reserva);
            cardsPanel.add(card);
        }

        JPanel addGuestOuterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        addGuestOuterPanel.setOpaque(false);
        addGuestOuterPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        JPanel addGuestComponentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Sem espaçamento interno
        addGuestComponentsPanel.setOpaque(false);


        JLabel addGuestLabel = new JLabel("Adicionar nova Reserva ");
        addGuestLabel.setOpaque(true);


        JButton plusButton = new JButton("+");
        plusButton.putClientProperty(FlatClientProperties.STYLE, "background: #D5BC00; foreground: #1E1E1E; arc: 999");
        plusButton.setOpaque(true);

        plusButton.addActionListener(e -> {
            AddBookingDialog dialog = new AddBookingDialog((Frame) SwingUtilities.getWindowAncestor(contentPanel));
            dialog.setVisible(true);

            if (dialog.isReservaAdicionada()) {
                cardsPanel.removeAll();
                List<Reserva> reservasAtual = reservaDAO.listarTodas(hospedeDAO,quartoDAO);
                for (Reserva reserva : reservasAtual) {
                    JPanel card = createReservaCard(reserva);
                    cardsPanel.add(card);
                }
                cardsPanel.revalidate();
                cardsPanel.repaint();
            }
        });

        addGuestComponentsPanel.add(addGuestLabel);
        addGuestComponentsPanel.add(plusButton);

        addGuestOuterPanel.add(addGuestComponentsPanel);
        contentPanel.add(addGuestOuterPanel, BorderLayout.SOUTH);

        return contentPanel;
    }


    private static JPanel createReservaCard(Reserva reserva) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.putClientProperty(FlatClientProperties.STYLE, "background: #FFFFFF; arc: 15");
        // Ajusta a largura para ocupar todo o espaço horizontal e altura fixa
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 20, 0), // Espaçamento entre cards
                BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding interno do card
        ));


        // Responsável
        JLabel responsavelLabel = new JLabel("Responsável: " + reserva.getHospede().getNome());
        responsavelLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2");
        responsavelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Quarto
        JLabel quartoLabel = new JLabel("Quarto: " + reserva.getQuarto().getNumero() + " (" + reserva.getQuarto().getTipo() + ")");
        quartoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Período
        JLabel periodoLabel = new JLabel("Período: " + reserva.getDataEntrada() + " a " + reserva.getDataSaida());
        periodoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Participantes
        JLabel participantesLabel = new JLabel("Participantes:");
        participantesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel participantesPanel = new JPanel();
        participantesPanel.setLayout(new BoxLayout(participantesPanel, BoxLayout.Y_AXIS));
        participantesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        participantesPanel.setOpaque(false);

        for (model.Hospede h : reserva.getParticipantes()) {
            JLabel nome = new JLabel("- " + h.getNome());
            nome.setAlignmentX(Component.LEFT_ALIGNMENT);
            participantesPanel.add(nome);
        }

        // Botão "Gerar Fatura" (a ação será implementada futuramente)
        JButton btnFatura = new JButton("Gerar Fatura");
        btnFatura.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnFatura.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        // Montagem final
        card.add(responsavelLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(quartoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(periodoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(participantesLabel);
        card.add(participantesPanel);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(btnFatura);

        return card;
    }
}