package view;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class ServicosPanel {
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Padding da área de conteúdo

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Servicos Extras");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #1E1E1E;");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(topAreaPanel, BorderLayout.NORTH);
        return contentPanel;
    }
}
