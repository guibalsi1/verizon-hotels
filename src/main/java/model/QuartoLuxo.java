package model;

public class QuartoLuxo implements Quarto {
    private final int numero;
    private boolean disponivel;
    private final double precoPorNoite;
    private final int quantidadePessoas;

    public QuartoLuxo(int numero, double precoPorNoite, int quantidadePessoas) {
        this.numero = numero;
        this.precoPorNoite = precoPorNoite;
        this.quantidadePessoas = quantidadePessoas;
        this.disponivel = true;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public boolean isDisponivel() {
        return disponivel;
    }

    @Override
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public double getPrecoPorNoite() {
        return precoPorNoite;
    }

    @Override
    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    @Override
    public String getTipo() {
        return "Luxo";
    }
}
