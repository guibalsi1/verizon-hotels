public class Funcionario extends Pessoa{
    private String cargo;
    private String login;

    public Funcionario(String nome, String cpf, String cargo, String login) {
        super(nome, cpf);
        this.cargo = cargo;
        this.login = login;
    }
    public String getCargo() {
        return cargo;
    }
}
