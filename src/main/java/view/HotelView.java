package view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import com.formdev.flatlaf.FlatLaf;
import control.utilities.ThemeManager;

public class HotelView extends JFrame {

    public HotelView() {
        setTitle("Verizon Hotels");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 670));
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(HospedesPanel.createContentPanel(), "hospedes");
        contentPanel.add(QuartosPanel.createContentPanel(), "quartos");
        contentPanel.add(FuncionarioPanel.createContentPanel(), "funcionarios");
        contentPanel.add(ReservasPanel.createContentPanel(), "reservas");
        contentPanel.add(RelatoriosPanel.createContentPanel(), "relatorios");
        contentPanel.add(new ConfigPanel(), "configuracoes");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navigationPanel = NavPanel.createNavigationPanel(contentPanel);

        mainPanel.add(navigationPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            InputStream fontStreamRegular = HotelView.class.getResourceAsStream("/fonts/Roboto-Regular.ttf");
            InputStream fontStreamBold = HotelView.class.getResourceAsStream("/fonts/Roboto-Bold.ttf");

            if (fontStreamRegular != null && fontStreamBold != null) {
                Font robotoRegular = Font.createFont(Font.TRUETYPE_FONT, fontStreamRegular);
                Font robotoBold = Font.createFont(Font.TRUETYPE_FONT, fontStreamBold);

                ge.registerFont(robotoRegular);
                ge.registerFont(robotoBold);
            } else {
                System.err.println("Arquivos de fonte não encontrados. Usando fonte padrão.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FlatLaf.registerCustomDefaultsSource("themes");
        ThemeManager.setupInitialTheme();

        SwingUtilities.invokeLater(() -> {
            HotelView hotelInterface = new HotelView();
            hotelInterface.setVisible(true);
        });
    }
}