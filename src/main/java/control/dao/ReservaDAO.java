package control.dao;

import control.ConexaoBanco;
import model.Hospede;
import model.Quarto;
import model.Reserva;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void salvar(Reserva r) {
        String sql = "INSERT INTO reservas (cpf_hospede, numero_quarto, data_entrada, data_saida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, r.getHospede().getCpf());
            stmt.setInt(2, r.getQuarto().getNumero());
            stmt.setString(3, r.getDataEntrada());
            stmt.setString(4, r.getDataSaida());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    r.setId(generatedKeys.getInt(1));
                }
            }

            String sqlParticipante = "INSERT INTO reservas_hospedes (reserva_id, cpf_hospede) VALUES (?, ?)";
            for (Hospede participante : r.getParticipantes()) {
                try (PreparedStatement stmtParticipante = ConexaoBanco.getConnection().prepareStatement(sqlParticipante)) {
                    stmtParticipante.setInt(1, r.getId());
                    stmtParticipante.setString(2, participante.getCpf());
                    stmtParticipante.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar reserva: " + e.getMessage());
        }
    }

    public List<Reserva> listarTodas(HospedeDAO hospedeDAO, QuartoDAO quartoDAO) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cpf = rs.getString("cpf_hospede");
                int numero = rs.getInt("numero_quarto");
                String entrada = rs.getString("data_entrada");
                String saida = rs.getString("data_saida");

                Hospede h = hospedeDAO.buscarPorCPF(cpf);
                Quarto q = quartoDAO.buscarPorNumero(numero);

                if (h != null && q != null) {
                    Reserva r = new Reserva(id, h, q, entrada, saida);
                    carregarParticipantes(r, hospedeDAO);
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas: " + e.getMessage());
        }
        return lista;
    }


    public List<Reserva> listarPorHospede(String cpfHospede, QuartoDAO quartoDAO, Hospede hospede) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE cpf_hospede = ?";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpfHospede);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int numeroQuarto = rs.getInt("numero_quarto");
                String entrada = rs.getString("data_entrada");
                String saida = rs.getString("data_saida");

                Quarto quarto = quartoDAO.buscarPorNumero(numeroQuarto);
                if (quarto != null) {
                    Reserva r = new Reserva(id, hospede, quarto, entrada, saida);
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas do model.Hospede: " + e.getMessage());
        }

        return lista;
    }

    private void carregarParticipantes(Reserva reserva, HospedeDAO hospedeDAO) throws SQLException {
        String sql = "SELECT cpf_hospede FROM reservas_hospedes WHERE reserva_id = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, reserva.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String cpfParticipante = rs.getString("cpf_hospede");
                Hospede participante = hospedeDAO.buscarPorCPF(cpfParticipante);
                if (participante != null) {
                    reserva.adicionarParticipante(participante);
                }
            }
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar reserva: " + e.getMessage());
        }
    }

    public boolean deletarPorHospede(String cpfHospede) {
        String sql = "DELETE FROM reservas WHERE cpf_hospede = ?";
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpfHospede);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar reservas do hóspede: " + e.getMessage());
        }
    }
    public boolean verificarDisponibilidade(int numeroQuarto, String dataEntrada, String dataSaida) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE numero_quarto = ? AND data_entrada < ? AND data_saida > ?";

        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, numeroQuarto);
            stmt.setString(2, dataSaida);
            stmt.setString(3, dataEntrada);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar disponibilidade do quarto: " + e.getMessage());
        }
        return false;
    }
    public Reserva buscaPorId(int id, HospedeDAO hospedeDAO, QuartoDAO quartoDAO) {
        String sql = "SELECT * FROM reservas WHERE id = ?";
        
        try (PreparedStatement stmt = ConexaoBanco.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String cpf = rs.getString("cpf_hospede");
                int numeroQuarto = rs.getInt("numero_quarto");
                String dataEntrada = rs.getString("data_entrada");
                String dataSaida = rs.getString("data_saida");
                
                Hospede hospede = hospedeDAO.buscarPorCPF(cpf);
                Quarto quarto = quartoDAO.buscarPorNumero(numeroQuarto);
                
                if (hospede != null && quarto != null) {
                    Reserva reserva = new Reserva(id, hospede, quarto, dataEntrada, dataSaida);
                    carregarParticipantes(reserva, hospedeDAO);
                    return reserva;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reserva por ID: " + e.getMessage());
        }
        
        return null;
    }
}