package model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Reserva {
    private Hospede hospede;
    private Quarto quarto;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private List<ServicoExtra> servicosExtras;
    private List<Hospede> participantes;

    public Hospede getHospede() {
        return hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public String getDataEntrada() {
        return checkIn.toString();
    }

    public String getDataSaida() {
        return checkOut.toString();
    }

    public Reserva(Hospede hospede, Quarto quarto, String dataEntrada, String dataSaida) {
        this.hospede = hospede;
        this.quarto = quarto;
        this.checkIn = LocalDate.parse(dataEntrada);
        this.checkOut = LocalDate.parse(dataSaida);
        this.participantes = new ArrayList<>();
    }

    public void adicionarParticipante(Hospede participante) {
        if (!participantes.contains(participante)) {
            participantes.add(participante);
        }
    }
}
