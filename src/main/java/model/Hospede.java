package model;

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

}
