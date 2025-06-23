package control.utilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;
import java.util.Map;


public class GeradorGraficos {
    public static ChartPanel criarGrafico(Map<String, Double> dados) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : dados.entrySet()) {
            dataset.addValue(entry.getValue(), "Faturamento Mensal", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Faturamento Mensal do Hotel",
                "Mês",
                "Faturamento (R$)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        chart.setBackgroundPaint(Color.white);
        chart.getPlot().setBackgroundPaint(Color.lightGray);

        return new ChartPanel(chart);
    }
}
