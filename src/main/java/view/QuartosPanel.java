package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.QuartoDAO;
import model.Quarto;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class QuartosPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    private static final String ICON_SIMPLES = "icons/Dollar.png";
    private static final String ICON_LUXO = "icons/Star.png";
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Padding da área de conteúdo

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Quartos");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #1E1E1E;");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(8, 0));
        searchBarPanel.setOpaque(false);
        JTextField searchField = new JTextField();
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Busca por Numero do Quarto...");

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

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 2, 20, 20)); // 2 colunas, espaçamento de 20px
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 25, 0));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(cardsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        QuartoDAO quartoDAO = new QuartoDAO();
        List<Quarto> quartos = quartoDAO.listar();

        for (Quarto quarto : quartos) {
            JPanel card = createQuartoCard(quarto);
            cardsPanel.add(card);
        }

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }
    private static JPanel createQuartoCard(Quarto quarto) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 150));
        card.setMaximumSize(new Dimension(300, 150));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        ImageIcon iconPng = null;
        if (quarto.getTipo().equals("Simples")) {
            iconPng = new ImageIcon(Objects.requireNonNull(QuartosPanel.class.getClassLoader().getResource(ICON_SIMPLES)));
            Image scaledImg = iconPng.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            iconPng = new ImageIcon(scaledImg);
        }
        else if (quarto.getTipo().equals("Luxo")) {
            iconPng = new ImageIcon(Objects.requireNonNull(QuartosPanel.class.getClassLoader().getResource(ICON_LUXO)));
            Image scaledImg = iconPng.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            iconPng = new ImageIcon(scaledImg);
        }

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.add(new JLabel(iconPng));
        card.add(iconPanel);

        JLabel quartoLabel = new JLabel(String.valueOf(quarto.getNumero()));
        quartoLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2");
        quartoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tipoLabel = new JLabel("Tipo: " + quarto.getTipo());
        tipoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel precoLabel = new JLabel(String.format("Preço por noite: R$ %.2f", quarto.getPrecoPorNoite()));
        precoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        if(quarto.isDisponivel()){
            card.putClientProperty(FlatClientProperties.STYLE, "background: #AEFF9A; foreground: #1E1E1E; arc: 15;");
        }
        else {
            card.putClientProperty(FlatClientProperties.STYLE, "background: #FF7777; foreground: #1E1E1E; arc: 15;");
        }
        card.add(quartoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(tipoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(precoLabel);

        return card;
    }
}
