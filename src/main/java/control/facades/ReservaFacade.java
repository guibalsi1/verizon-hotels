package control.facades;
import control.dao.HospedeDAO;
import control.dao.QuartoDAO;
import control.dao.ReservaDAO;
import model.Hospede;
import model.Quarto;
import model.Reserva;
import model.exceptions.ReservaInvalidaException;

import java.util.List;

public class ReservaFacade {
    private final ReservaDAO reservaDAO;
    private final HospedeDAO hospedeDAO;
    private final QuartoDAO quartoDAO;

    public ReservaFacade(ReservaDAO reservaDAO, HospedeDAO hospedeDAO, QuartoDAO quartoDAO) {
        this.reservaDAO = reservaDAO;
        this.hospedeDAO = hospedeDAO;
        this.quartoDAO = quartoDAO;
    }

    public Reserva criarReserva(int id, String cpfResponsavel, int numeroQuarto, String dataEntrada, String dataSaida, List<String> cpfsParticipantes) throws ReservaInvalidaException {
        try {

            // Buscar o hóspede responsável
            Hospede responsavel = hospedeDAO.buscarPorCPF(cpfResponsavel, quartoDAO, reservaDAO);
            if (responsavel == null) {
                throw new ReservaInvalidaException("Responsável não encontrado: " + cpfResponsavel);
            }

            // Buscar o quarto
            Quarto quarto = quartoDAO.buscarPorNumero(numeroQuarto);
            if (quarto == null || !quarto.isDisponivel()) {
                throw new ReservaInvalidaException("Quarto indisponível ou inexistente: " + numeroQuarto);
            }

            // Criar a reserva
            Reserva reserva = new Reserva(id,responsavel, quarto, dataEntrada, dataSaida);

            // Adicionar o responsável como participante
            reserva.adicionarParticipante(responsavel);

            // Buscar e adicionar os demais participantes
            for (String cpf : cpfsParticipantes) {
                if (cpf.equals(cpfResponsavel)) continue; // já adicionado
                Hospede participante = hospedeDAO.buscarPorCPF(cpf, quartoDAO, reservaDAO);
                if (participante != null) {
                    reserva.adicionarParticipante(participante);
                } else {
                    System.out.println("Participante ignorado (não encontrado): " + cpf);
                }
            }

            // Persistir no banco de dados
            reservaDAO.salvar(reserva);

            // Atualizar disponibilidade do quarto (se quiser)
            quarto.setDisponivel(false);
            quartoDAO.atualizar(quarto);

            return reserva;

        } catch (Exception e) {
            throw new ReservaInvalidaException("Erro ao criar reserva: " + e.getMessage(), e);
        }
    }
}