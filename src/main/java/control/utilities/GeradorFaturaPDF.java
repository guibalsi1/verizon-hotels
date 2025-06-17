package control.utilities;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import model.Hospede;
import model.Reserva;

import javax.swing.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class GeradorFaturaPDF {
    private static final String LOGO_PATH = "src/main/resources/icons/Logo.png";

    public void gerar(Reserva reserva) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Fatura em PDF");
        fileChooser.setSelectedFile(new java.io.File("Fatura_Reserva_" + reserva.getId() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            Font fontCorpo = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);

            Image logo = Image.getInstance(LOGO_PATH);
            logo.scaleAbsolute(100, 100);
            document.add(logo);
            Paragraph titulo = new Paragraph("Fatura - Verizon Hotels", fontTitulo);
            titulo.setAlignment(Element.ALIGN_LEFT);
            document.add(titulo);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Detalhes da Reserva", fontSubtitulo));
            document.add(new Paragraph("ID da Reserva: " + reserva.getId(), fontCorpo));
            document.add(new Paragraph("Hóspede Responsável: " + reserva.getHospede().getNome(), fontCorpo));
            document.add(new Paragraph("CPF: " + reserva.getHospede().getCpf(), fontCorpo));
            document.add(new Paragraph("Quarto: " + reserva.getQuarto().getNumero() + " (Tipo: " + reserva.getQuarto().getTipo() + ")", fontCorpo));
            document.add(new Paragraph("Período: " + reserva.getDataEntrada() + " a " + reserva.getDataSaida(), fontCorpo));

            if (reserva.getParticipantes().size() > 1) {
                document.add(new Paragraph("Participantes Adicionais:", fontCorpo));
                for (Hospede participante : reserva.getParticipantes()) {
                    if (!participante.getCpf().equals(reserva.getHospede().getCpf())) {
                        document.add(new Paragraph("- " + participante.getNome(), fontCorpo));
                    }
                }
            }
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Detalhes do Custo", fontSubtitulo));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkIn = LocalDate.parse(reserva.getDataEntrada(), formatter);
            LocalDate checkOut = LocalDate.parse(reserva.getDataSaida(), formatter);

            long numeroDiarias = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (numeroDiarias == 0) numeroDiarias = 1;

            double precoPorNoite = reserva.getQuarto().getPrecoPorNoite();
            double totalAPagar = numeroDiarias * precoPorNoite;

            document.add(new Paragraph(String.format("Preço por diária: R$ %.2f", precoPorNoite), fontCorpo));
            document.add(new Paragraph("Número de diárias: " + numeroDiarias, fontCorpo));
            document.add(new Paragraph(" "));

            Paragraph total = new Paragraph(String.format("Total a Pagar: R$ %.2f", totalAPagar), fontTotal);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

            JOptionPane.showMessageDialog(null, "Fatura em PDF gerada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar a fatura em PDF: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}