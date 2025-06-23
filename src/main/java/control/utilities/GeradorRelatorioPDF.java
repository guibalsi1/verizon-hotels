package control.utilities;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import control.dao.*;

import javax.swing.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorRelatorioPDF {
    private static final String LOGO_PATH = "src/main/resources/icons/Logo.png";

    public void gerar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório do Hotel");
        fileChooser.setSelectedFile(new java.io.File("Relatorio_Hotel_Verizon_" + LocalDate.now() + ".pdf"));

        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) filePath += ".pdf";

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            Font fontCorpo = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Image logo = Image.getInstance(LOGO_PATH);
            logo.scaleAbsolute(100, 100);
            document.add(logo);
            document.add(new Paragraph("Relatório Geral do Hotel", fontTitulo));
            document.add(new Paragraph("Gerado em: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fontCorpo));
            document.add(Chunk.NEWLINE);

            HospedeDAO hospedeDAO = new HospedeDAO();
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            QuartoDAO quartoDAO = new QuartoDAO();
            ReservaDAO reservaDAO = new ReservaDAO();

            int totalHospedes = hospedeDAO.listar().size();
            int totalFuncionarios = funcionarioDAO.listar().size();
            int totalQuartos = quartoDAO.listar().size();
            long quartosOcupados = quartoDAO.listar().stream().filter(q -> !q.isDisponivel()).count();
            int totalReservas = reservaDAO.listarTodas(hospedeDAO, quartoDAO).size();

            document.add(new Paragraph("Resumo Operacional", fontSubtitulo));
            document.add(new Paragraph("Total de Hóspedes Cadastrados: " + totalHospedes, fontCorpo));
            document.add(new Paragraph("Total de Funcionários: " + totalFuncionarios, fontCorpo));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Ocupação de Quartos", fontSubtitulo));
            document.add(new Paragraph("Total de Quartos no Hotel: " + totalQuartos, fontCorpo));
            document.add(new Paragraph(String.format("Quartos Ocupados Atualmente: %d (%d%%)", quartosOcupados, (totalQuartos > 0 ? (100 * quartosOcupados / totalQuartos) : 0)), fontCorpo));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Atividade de Reservas", fontSubtitulo));
            document.add(new Paragraph("Total de Reservas no Histórico: " + totalReservas, fontCorpo));
            document.add(Chunk.NEWLINE);

            document.close();
            JOptionPane.showMessageDialog(null, "Relatório em PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório PDF: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}