package control.dao;

import control.ConexaoBanco;
import model.Quarto;
import model.QuartoLuxo;
import model.QuartoSimples;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuartoDAO {

    public void salvar(Quarto quarto) {
        String sql = "INSERT INTO quartos (numero, tipo, disponivel, preco, quantidade_pessoas) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)){
            stmt.setInt(1, quarto.getNumero());
            stmt.setString(2, quarto.getTipo());
            stmt.setBoolean(3, quarto.isDisponivel());
            stmt.setDouble(4, quarto.getPrecoPorNoite());
            stmt.setInt(5, quarto.getQuantidadePessoas());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar quarto: " + e.getMessage());
        }
    }

    public void atualizar(Quarto quarto) {
        String sql = "UPDATE quartos SET tipo = ?, disponivel = ?, preco = ?, quantidade_pessoas = ? WHERE numero = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setString(1, quarto.getTipo());
            stmt.setBoolean(2, quarto.isDisponivel());
            stmt.setDouble(3, quarto.getPrecoPorNoite());
            stmt.setInt(4, quarto.getQuantidadePessoas());
            stmt.setInt(5, quarto.getNumero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quarto: " + e.getMessage());
        }
    }

    public List<Quarto> listar() {
        List<Quarto> lista = new ArrayList<>();
        String sql = "SELECT * FROM quartos";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int numero = rs.getInt("numero");
                String tipo = rs.getString("tipo");
                double preco = rs.getDouble("preco");
                int qtd = rs.getInt("quantidade_pessoas");
                boolean disponivel = rs.getBoolean("disponivel");
                Quarto quarto = tipo.equalsIgnoreCase("Simples") ? new QuartoSimples(numero, preco, qtd) : new QuartoLuxo(numero, preco, qtd);
                quarto.setDisponivel(disponivel);
                lista.add(quarto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar quartos: " + e.getMessage());
        }
        return lista;
    }

    public Quarto buscarPorNumero(int numero) {
        String sql = "SELECT * FROM quartos WHERE numero = ?";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)){
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                double preco = rs.getDouble("preco");
                int qtd = rs.getInt("quantidade_pessoas");
                boolean disponivel = rs.getBoolean("disponivel");
                Quarto quarto = tipo.equalsIgnoreCase("Simples") ? new QuartoSimples(numero, preco, qtd) : new QuartoLuxo(numero, preco, qtd);
                quarto.setDisponivel(disponivel);
                return quarto;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar quarto: " + e.getMessage());
        }
        return null;
    }
    public List<Quarto> listarQuartosDisponiveis(String dataEntrada, String dataSaida) {
        List<Quarto> lista = new ArrayList<>();
        // Este SQL seleciona todos os quartos cujo número NÃO ESTÁ na lista de quartos
        // que possuem uma reserva conflitante no período desejado.
        String sql = "SELECT * FROM quartos WHERE numero NOT IN (" +
                "  SELECT numero_quarto FROM reservas WHERE data_entrada < ? AND data_saida > ?" +
                ")";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setString(1, dataSaida);
            stmt.setString(2, dataEntrada);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int numero = rs.getInt("numero");
                String tipo = rs.getString("tipo");
                double preco = rs.getDouble("preco");
                int qtd = rs.getInt("quantidade_pessoas");
                boolean disponivel = rs.getBoolean("disponivel"); // Embora a lógica de data seja mais importante, mantemos.
                Quarto quarto = tipo.equalsIgnoreCase("Simples") ? new QuartoSimples(numero, preco, qtd) : new QuartoLuxo(numero, preco, qtd);
                quarto.setDisponivel(disponivel);
                lista.add(quarto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar quartos disponíveis: " + e.getMessage());
        }
        return lista;
    }
}
