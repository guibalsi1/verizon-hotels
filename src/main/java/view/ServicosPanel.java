package view;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class ServicosPanel {
    private static final String ICON_BUSCAR = "icons/Search.png";
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Padding da área de conteúdo

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Servicos Extras");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "title-label");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);

        return contentPanel;
    }
}
