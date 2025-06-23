package control.dao;

import control.ConexaoBanco;
import model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private final Connection connection;

    public FuncionarioDAO() {
        this.connection = ConexaoBanco.getConnection();
    }


    /**
     * Salva um funcionario no banco de dados SQLite
     * @param funcionario Classe Funcionario
     *
     */
    public void salvar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionarios (cpf, nome, cargo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getNome());
            stmt.setString(3, funcionario.getCargo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar funcionário: " + e.getMessage());
        }
    }

    /**
     * Faz a busca do funcionario no banco de dados
     * @param cpf chave primaria da tabela funcionarios
     * @return o funcionario com o cpf desejado ou null se não encontrado
     */
    public Funcionario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM funcionarios WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("cargo")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista os funcionarios existentes no banco de dados
     * @return Retorna um ArrayList de Funcionarios
     */
    public List<Funcionario> listar() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Funcionario f = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("cargo")
                );
                lista.add(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Faz a exclusão do funcionario desejado no banco de dados
     * @param cpf chave primaria da tabela funcionarios
     * @return true se a exclusão foi feita, false caso não foi encontrado o funcionario
     */
    public boolean deletar(String cpf) {
        String sql = "DELETE FROM funcionarios WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário: " + e.getMessage());
        }
    }
}