package model;

import model.exceptions.ReservaInvalidaException;

import java.util.ArrayList;
import java.util.List;

public class Hospede extends Pessoa {
    private String telefone;
    private List<Reserva> reservas;

    public Hospede(String cpf, String nome, String telefone) {
        super(nome, cpf);
        this.telefone = telefone;
        this.reservas = new ArrayList<>();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

//    public void reservar(Quarto quarto, String dataEntrada, String dataSaida) throws ReservaInvalidaException {
//        if (!quarto.isDisponivel()) {
//            throw new ReservaInvalidaException("O quarto " + quarto.getNumero() + " está indisponível.");
//        }
//
//        if (dataEntrada.compareTo(dataSaida) >= 0) {
//            throw new ReservaInvalidaException("Data de entrada deve ser anterior à data de saída.");
//        }
//
//        // Marca como indisponível (regra de negócio)
//        quarto.setDisponivel(false);
//
//        Reserva nova = new Reserva(id,this, quarto, dataEntrada, dataSaida);
//        this.reservas.add(nova);
//    }
}
