package view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class NavPanel extends JPanel {
    private static final String ICON_HOSPEDES = "icons/Users.png";
    private static final String ICON_FUNCIONARIOS = "icons/Target.png";
    private static final String ICON_QUARTOS = "icons/Home.png";
    private static final String ICON_RESERVAS = "icons/Book.png";
    private static final String ICON_SERVICOS = "icons/Star.png";
    private static final String ICON_RELATORIOS = "icons/Table.png";
    private static final String ICON_CONFIGURACOES = "icons/Settings.png";
    private static final String ICON_LOGO = "icons/Logo.png";

    public static JPanel createNavigationPanel(JPanel contentPanel) {
        JPanel navPanel = new JPanel();
        navPanel.setName("navigationPanel");
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(240, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ImageIcon logoIcon = null;
        logoIcon = new ImageIcon(Objects.requireNonNull(NavPanel.class.getClassLoader().getResource(ICON_LOGO)));
        Image scaledImg = logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledImg);
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoPanel.setMaximumSize(new Dimension( Integer.MAX_VALUE, 100));
        logoPanel.setOpaque(false);
        logoPanel.add(new JLabel(logoIcon));
        navPanel.add(logoPanel);

        String[] navText = {
                "Hóspedes", "Funcionários", "Quartos", "Reservas",
                "Serviços Extras", "Relatórios", "Configurações"
        };
        String[] iconPaths = {
                ICON_HOSPEDES, ICON_FUNCIONARIOS, ICON_QUARTOS, ICON_RESERVAS,
                ICON_SERVICOS, ICON_RELATORIOS, ICON_CONFIGURACOES
        };
        String[] cardNames = {
                "hospedes", "funcionarios", "quartos", "reservas",
                "servicos", "relatorios", "configuracoes"
        };

        for (int i = 0; i < navText.length; i++) {
            ImageIcon icon = null;
            try {
                java.net.URL imgUrl = NavPanel.class.getClassLoader().getResource(iconPaths[i]);
                if (imgUrl != null) {
                    ImageIcon originalIcon = new ImageIcon(imgUrl);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                } else {
                    System.err.println("Ícone de navegação não encontrado: " + iconPaths[i]);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar ícone de navegação " + iconPaths[i] + ": " + e.getMessage());
            }

            JButton navButton = new JButton(navText[i], icon);
            navButton.setHorizontalAlignment(SwingConstants.LEFT);
            navButton.setIconTextGap(10);
            navButton.setMargin(new Insets(5,5,0,0));
            navButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, navButton.getPreferredSize().height + 10));

            String cardName = cardNames[i];
            navButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, cardName);
            });

            navPanel.add(navButton);
            if (i < navText.length - 1) {
                navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        return navPanel;
    }
}
