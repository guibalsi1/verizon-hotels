package view;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

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
        contentPanel.add(ServicosPanel.createContentPanel(), "servicos");
        contentPanel.add(RelatoriosPanel.createContentPanel(), "relatorios");
        contentPanel.add(ConfigPanel.createContentPanel(), "configuracoes");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navigationPanel = NavPanel.createNavigationPanel(contentPanel);

        mainPanel.add(navigationPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            HotelView hotelInterface = new HotelView();
            hotelInterface.setVisible(true);
        });
    }
}