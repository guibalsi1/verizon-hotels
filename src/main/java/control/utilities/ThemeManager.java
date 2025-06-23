package control.utilities;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class ThemeManager {

    private static final String PREF_KEY_THEME = "app_theme";
    private static final String THEME_LIGHT = "light";
    private static final String THEME_DARK = "dark";

    private static final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);

    public static void setupInitialTheme() {
        String theme = prefs.get(PREF_KEY_THEME, THEME_LIGHT);
        if (theme.equals(THEME_DARK)) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
    }

    public static void toggleTheme(boolean useDark, JFrame mainFrame) {
        String theme = useDark ? THEME_DARK : THEME_LIGHT;
        prefs.put(PREF_KEY_THEME, theme);

        try {
            if (useDark) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Erro ao trocar o tema: " + e.getMessage());
        }
    }
    public static boolean isDarkMode() {
        return prefs.get(PREF_KEY_THEME, THEME_LIGHT).equals(THEME_DARK);
    }
}