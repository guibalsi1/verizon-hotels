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
}
