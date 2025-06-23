package view;

import com.formdev.flatlaf.FlatClientProperties;
import control.utilities.GeradorGraficos;
import control.facade.ReservaFacade;
import control.utilities.GeradorRelatorioPDF;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RelatoriosPanel {
    public static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel topAreaPanel = new JPanel(new BorderLayout(0, 15));
        topAreaPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Relatórios");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #F1DB52;");

        JButton btnGerarPdf = new JButton("Gerar Relatório em PDF");
        btnGerarPdf.addActionListener(e -> {
            GeradorRelatorioPDF gerador = new GeradorRelatorioPDF();
            gerador.gerar();
        });

        topAreaPanel.add(titleLabel, BorderLayout.WEST);
        topAreaPanel.add(btnGerarPdf, BorderLayout.EAST);

        contentPanel.add(topAreaPanel, BorderLayout.NORTH);

        ReservaFacade reservaFacade = new ReservaFacade();
        Map<String, Double> dadosFaturamento = reservaFacade.getFaturamentoMensal();

        if (dadosFaturamento.isEmpty()) {
            JLabel mensagem = new JLabel("Não há dados de faturamento para exibir.", SwingConstants.CENTER);
            mensagem.putClientProperty(FlatClientProperties.STYLE, "font: +1;");
            contentPanel.add(mensagem, BorderLayout.CENTER);
        } else {
            ChartPanel chartPanel = GeradorGraficos.criarGrafico(dadosFaturamento);
            contentPanel.add(chartPanel, BorderLayout.CENTER);
        }

        return contentPanel;
    }
}