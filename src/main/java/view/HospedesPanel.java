package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.dao.HospedeDAO;
import model.Hospede;
import view.dialogs.AddGuestDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class HospedesPanel extends JPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    private static final String ICON_EXCLUIR = "icons/Trash.png";
    
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Hóspedes");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #F1DB52;");
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

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 2, 20, 20));
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

        HospedeDAO hospedeDAO = new HospedeDAO();
        List<Hospede> hospedes = hospedeDAO.listar();

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

        searchButton.addActionListener(e -> {
            String cpfBusca = searchField.getText().trim();

            if (cpfBusca.isEmpty()) {
                cardsPanel.removeAll();
                List<Hospede> todosHospedes = hospedeDAO.listar();
                for (Hospede h : todosHospedes) {
                    JPanel card = createHospedeCard(h);
                    cardsPanel.add(card);
                }
            } else {
                try {
                    cpfBusca = cpfBusca.replaceAll("[^0-9]", "");

                    Hospede hospede = hospedeDAO.buscarPorCPF(cpfBusca);

                    cardsPanel.removeAll();

                    if (hospede != null) {
                        JPanel card = createHospedeCard(hospede);
                        cardsPanel.add(card);
                    } else {
                        JLabel mensagem = new JLabel("Nenhum hóspede encontrado com este CPF");
                        mensagem.setHorizontalAlignment(JLabel.CENTER);
                        cardsPanel.add(mensagem);
                    }
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(
                            contentPanel,
                            "Erro ao buscar hóspede: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            cardsPanel.revalidate();
            cardsPanel.repaint();
        });

        JButton finalSearchButton = searchButton;
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    finalSearchButton.doClick();
                }
            }
        });

        JLabel addGuestLabel = new JLabel("Adicionar novo Hóspede ");
        addGuestLabel.setOpaque(true);

        JButton plusButton = new JButton("+");
        plusButton.putClientProperty(FlatClientProperties.STYLE, "background: #D5BC00; foreground: #1E1E1E; arc: 999");
        plusButton.setOpaque(true);

        plusButton.addActionListener(e -> {
            AddGuestDialog dialog = new AddGuestDialog((Frame) SwingUtilities.getWindowAncestor(contentPanel));
            dialog.setVisible(true);

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
        card.setPreferredSize(new Dimension(300, 150));
        card.setMaximumSize(new Dimension(300, 150));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));


        JLabel nomeLabel = new JLabel(hospede.getNome());
        nomeLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2");
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cpfLabel = new JLabel("CPF: " + hospede.getCpf());
        cpfLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel telefoneLabel = new JLabel("Tel: " + hospede.getTelefone());
        telefoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        ImageIcon deleteIconPng = null;
        deleteIconPng = new ImageIcon(Objects.requireNonNull(HospedesPanel.class.getClassLoader().getResource(ICON_EXCLUIR)));
        JButton deleteButton = new JButton(deleteIconPng);
        deleteButton.putClientProperty(FlatClientProperties.STYLE, "background: #FFFFFF; arc: 999");
        deleteButton.setOpaque(true);
        buttonPanel.add(deleteButton);
        deleteButton.addActionListener(e -> {
            int confirma = JOptionPane.showConfirmDialog(
                SwingUtilities.getWindowAncestor(card),
                "Tem certeza que deseja excluir o hóspede " + hospede.getNome() + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirma == JOptionPane.YES_OPTION) {
                try {
                    HospedeDAO hospedeDAO = new HospedeDAO();
                    boolean deletado = hospedeDAO.deletar(hospede.getCpf());
                    
                    if (deletado) {
                        Container cardsPanel = card.getParent();
                        cardsPanel.remove(card);
                        cardsPanel.revalidate();
                        cardsPanel.repaint();
                        
                        JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(card),
                            "Hóspede excluído com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(card),
                            "Não foi possível excluir o hóspede.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(card),
                        "Erro ao excluir hóspede: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        card.add(nomeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(cpfLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(telefoneLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }
}