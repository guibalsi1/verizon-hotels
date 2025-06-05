package model;

public class Funcionario extends Pessoa {
    private String cargo;

    public Funcionario(String cpf, String nome, String cargo) {
        super(nome, cpf);
        this.cargo = cargo;
    }
    public String getCargo() {
        return cargo;
    }
}
