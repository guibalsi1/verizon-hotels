import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospedeDAO {
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

    public Hospede buscarPorCPF(String cpf, QuartoDAO quartoDAO, ReservaDAO reservaDAO) {
        String sql = "SELECT * FROM hospedes WHERE cpf = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)){
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Hospede h = new Hospede(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("telefone")
                );

                List<Reserva> reservas = reservaDAO.listarPorHospede(cpf, quartoDAO, h);
                h.setReservas(reservas);

                return h;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar hospede por CPF: " + e.getMessage());
        }
        return null;
    }
}
