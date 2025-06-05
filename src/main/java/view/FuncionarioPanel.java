package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.FuncionarioDAO;
import model.Funcionario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FuncionarioPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Padding da área de conteúdo

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Funcionários");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "title-label");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(8, 0));
        searchBarPanel.setOpaque(false);
        JTextField searchField = new JTextField();
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Busca por Nome,Cargo...");

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

        JPanel cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20,0,25,0));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        List<Funcionario> funcionarios = funcionarioDAO.listar();

        for (Funcionario funcionario : funcionarios) {
            JPanel card = createFuncionarioCard(funcionario);
            cardsPanel.add(card);
        }

        contentPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel addWorkerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        addWorkerPanel.setOpaque(false);
        addWorkerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        JPanel addWorkerComponentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        addWorkerComponentPanel.setOpaque(false);

        JLabel addWorkerLabel = new JLabel("Adicionar novo Funcionário");
        addWorkerLabel.setOpaque(true);

        JButton plusButton = new JButton("+");
        plusButton.putClientProperty(FlatClientProperties.STYLE, "background: #D5BC00; foreground: #1E1E1E; arc: 999");
        plusButton.setOpaque(true);

        plusButton.addActionListener( e -> {
            AddWorkerDialog dialog = new AddWorkerDialog((Frame) SwingUtilities.getWindowAncestor(contentPanel));
            dialog.setVisible(true);

            if (dialog.isWorkerAdded()) {
                cardsPanel.removeAll();
                List<Funcionario> funcionariosAtual = funcionarioDAO.listar();
                for (Funcionario funcionario : funcionariosAtual) {
                    JPanel card = createFuncionarioCard(funcionario);
                    cardsPanel.add(card);
                }
                cardsPanel.revalidate();
                cardsPanel.repaint();
            }
        });

        addWorkerComponentPanel.add(addWorkerLabel);
        addWorkerComponentPanel.add(plusButton);

        addWorkerPanel.add(addWorkerComponentPanel);
        contentPanel.add(addWorkerPanel, BorderLayout.SOUTH);

        return contentPanel;
    }
    private static JPanel createFuncionarioCard(Funcionario funcionario) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.putClientProperty(FlatClientProperties.STYLE, "background: #FFFFFF; arc: 15");
        card.setPreferredSize(new Dimension(250, 150));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel nomeLabel = new JLabel(funcionario.getNome());
        nomeLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2");
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cpfLabel = new JLabel("CPF: " + funcionario.getCpf());
        cpfLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cargoLabel = new JLabel("Cargo: " + funcionario.getCargo());
        cargoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(nomeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(cpfLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(cargoLabel);

        return card;
    }
}
