package control.dao;

import control.ConexaoBanco;
import model.Hospede;
import model.Reserva;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospedeDAO {

    /**
     * Salva um Hospede no banco de dados SQLite
     * @param hospede Classe Hospede
     */
    public void salvar(Hospede hospede) {
        String sql = "INSERT INTO hospedes (cpf, nome, telefone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)){
            stmt.setString(1, hospede.getCpf());
            stmt.setString(2, hospede.getNome());
            stmt.setString(3, hospede.getTelefone());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar hospede: " + e.getMessage());
        }
    }

    /**
     * Lista todos os hospedes do banco de dados
     * @return Retorna um ArrayList de hospedes
     */
    public List<Hospede> listar() {
        List<Hospede> lista = new ArrayList<>();
        String sql = "SELECT * FROM hospedes";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                Hospede hospede = new Hospede(rs.getString("cpf"), rs.getString("nome"), rs.getString("telefone"));
                lista.add(hospede);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar hospedes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Faz a busca do Funcionario no banco de dados
     * @param cpf chave primaria da tabela hospedes
     * @return o hospede com o cpf desejado ou null se não encontrado
     */
    public Hospede buscarPorCPF(String cpf) {
        String sql = "SELECT * FROM hospedes WHERE cpf = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)){
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Hospede(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("telefone")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar hospede por CPF: " + e.getMessage());
        }
        return null;
    }

    /**
     * Faz a exclusão de um hospede desejado no banco de dados
     * @param cpf chave primaria da tabela hospedes
     * @return Retorna true se a exclusão foi feita, false caso não foi encontrado o hospede
     */
    public boolean deletar(String cpf) {
        String sql = "DELETE FROM hospedes WHERE cpf = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpf);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar hóspede: " + e.getMessage());
        }
    }

    /**
     * Lista todos os hospedes disponiveis na data selecionada
     * @param dataEntrada Data do inicio da procura
     * @param dataSaida Data do ultimo dia a ser procurado
     * @return Retonra uma lista de hospedes ou null caso não encontrado nenhum hospede
     */
    public List<Hospede> listarHospedesDisponiveis(String dataEntrada, String dataSaida) {
        List<Hospede> lista = new ArrayList<>();
        String sql = "SELECT * FROM hospedes WHERE cpf NOT IN (" +
                "  SELECT DISTINCT cpf_hospede FROM (" +
                "      SELECT cpf_hospede FROM reservas WHERE data_entrada < ? AND data_saida > ? " +
                "      UNION " +
                "      SELECT rh.cpf_hospede FROM reservas_hospedes rh JOIN reservas r ON rh.reserva_id = r.id WHERE r.data_entrada < ? AND r.data_saida > ?" +
                "  )" +
                ")";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setString(1, dataSaida);
            stmt.setString(2, dataEntrada);
            stmt.setString(3, dataSaida);
            stmt.setString(4, dataEntrada);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Hospede(rs.getString("cpf"), rs.getString("nome"), rs.getString("telefone")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar hóspedes disponíveis: " + e.getMessage());
        }
        return lista;
    }
}