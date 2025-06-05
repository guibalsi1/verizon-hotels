package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.HospedeDAO;
import model.Hospede;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HospedesPanel extends JPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Hóspedes");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "title-label");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(8, 0));
        searchBarPanel.setOpaque(false);
        JTextField searchField = new JTextField();
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Busca por CPF...");

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
        JPanel cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 25, 0));

        // Adiciona ScrollPane
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Busca hóspedes do banco de dados
        HospedeDAO hospedeDAO = new HospedeDAO();
        List<Hospede> hospedes = hospedeDAO.listar();

        // Cria cards para cada hóspede
        for (Hospede hospede : hospedes) {
            JPanel card = createHospedeCard(hospede);
            cardsPanel.add(card);
        }

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel addGuestOuterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        addGuestOuterPanel.setOpaque(false);
        addGuestOuterPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        JPanel addGuestComponentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        addGuestComponentsPanel.setOpaque(false);

        JLabel addGuestLabel = new JLabel("Adicionar novo Hóspede ");
        addGuestLabel.setOpaque(true);

        JButton plusButton = new JButton("+");
        plusButton.putClientProperty(FlatClientProperties.STYLE, "background: #D5BC00; foreground: #1E1E1E; arc: 999");
        plusButton.setOpaque(true);
        
        // Adiciona ação ao botão
        plusButton.addActionListener(e -> {
            AdicionarHospedeDialog dialog = new AdicionarHospedeDialog((Frame) SwingUtilities.getWindowAncestor(contentPanel));
            dialog.setVisible(true);
            
            // Se um novo hóspede foi adicionado, atualiza a lista
            if (dialog.isHospedeAdicionado()) {
                cardsPanel.removeAll();
                List<Hospede> hospedesAtual = hospedeDAO.listar();
                for (Hospede hospede : hospedesAtual) {
                    JPanel card = createHospedeCard(hospede);
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

    private static JPanel createHospedeCard(Hospede hospede) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.putClientProperty(FlatClientProperties.STYLE, "background: #FFFFFF; arc: 15");
        card.setPreferredSize(new Dimension(250, 150));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Nome do hóspede
        JLabel nomeLabel = new JLabel(hospede.getNome());
        nomeLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2");
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // CPF
        JLabel cpfLabel = new JLabel("CPF: " + hospede.getCpf());
        cpfLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Telefone
        JLabel telefoneLabel = new JLabel("Tel: " + hospede.getTelefone());
        telefoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(nomeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(cpfLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(telefoneLabel);

        return card;
    }
}

// Classe WrapLayout para melhor organização dos cards
class WrapLayout extends FlowLayout {
    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getWidth();
            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
            int x = insets.left + hgap;
            int y = insets.top + vgap;
            int rowHeight = 0;

            int nmembers = target.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    if (x + d.width > maxWidth) {
                        x = insets.left + hgap;
                        y += vgap + rowHeight;
                        rowHeight = 0;
                    }
                    x += d.width + hgap;
                    rowHeight = Math.max(rowHeight, d.height);
                }
            }
            y += rowHeight + vgap;
            return new Dimension(targetWidth, y + insets.bottom);
        }
    }
}