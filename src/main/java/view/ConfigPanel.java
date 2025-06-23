package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.utilities.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {

    public ConfigPanel() {
        super(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Configurações");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #FAED27;");
        topAreaPanel.add(titleLabel, BorderLayout.NORTH);
        add(topAreaPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        JLabel themeLabel = new JLabel("Tema da Aplicação:");
        optionsPanel.add(themeLabel);
        optionsPanel.add(Box.createHorizontalStrut(10));

        JToggleButton themeToggle = new JToggleButton("Modo Escuro");
        themeToggle.setSelected(ThemeManager.isDarkMode());
        themeToggle.addActionListener(e -> {
            boolean useDark = themeToggle.isSelected();
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ThemeManager.toggleTheme(useDark, mainFrame);
        });

        optionsPanel.add(themeToggle);
        add(optionsPanel, BorderLayout.CENTER);
    }
}
