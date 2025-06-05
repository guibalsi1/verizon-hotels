package view;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class QuartosPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Padding da área de conteúdo

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Quartos");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "title-label");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(8, 0));
        searchBarPanel.setOpaque(false);
        JTextField searchField = new JTextField();
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Busca por número do quarto..");

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

        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        for (int i = 0; i < 4; i++) {
            JPanel card = new JPanel();
            card.putClientProperty(FlatClientProperties.STYLE, "background: #FF0000; arc: 15");
            card.setPreferredSize(new Dimension(200, 150));
            cardsPanel.add(card);
        }
        contentPanel.add(cardsPanel, BorderLayout.CENTER);

        JPanel addGuestOuterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        addGuestOuterPanel.setOpaque(false);
        addGuestOuterPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        JPanel addGuestComponentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Sem espaçamento interno
        addGuestComponentsPanel.setOpaque(false);


        JLabel addGuestLabel = new JLabel("Adicionar novo Hóspede ");
        addGuestLabel.setOpaque(true);


        JButton plusButton = new JButton("+");
        plusButton.putClientProperty(FlatClientProperties.STYLE, "background: #FF0000; foreground: #FFFFFF; arc: 15");
        plusButton.setOpaque(true);

        addGuestComponentsPanel.add(addGuestLabel);
        addGuestComponentsPanel.add(plusButton);

        addGuestOuterPanel.add(addGuestComponentsPanel);
        contentPanel.add(addGuestOuterPanel, BorderLayout.SOUTH);

        return contentPanel;
    }
}
