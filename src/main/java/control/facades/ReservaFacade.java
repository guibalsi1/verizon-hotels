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
            Hospede responsavel = hospedeDAO.buscarPorCPF(cpfResponsavel);
            if (responsavel == null) {
                throw new ReservaInvalidaException("Responsável não encontrado: " + cpfResponsavel);
            }

            Quarto quarto = quartoDAO.buscarPorNumero(numeroQuarto);
            if (quarto == null || !quarto.isDisponivel()) {
                throw new ReservaInvalidaException("Quarto indisponível ou inexistente: " + numeroQuarto);
            }

            Reserva reserva = new Reserva(id,responsavel, quarto, dataEntrada, dataSaida);

            reserva.adicionarParticipante(responsavel);

            for (String cpf : cpfsParticipantes) {
                if (cpf.equals(cpfResponsavel)) continue;
                Hospede participante = hospedeDAO.buscarPorCPF(cpf);
                if (participante != null) {
                    reserva.adicionarParticipante(participante);
                } else {
                    System.out.println("Participante ignorado (não encontrado): " + cpf);
                }
            }

            reservaDAO.salvar(reserva);
            quarto.setDisponivel(false);
            quartoDAO.atualizar(quarto);

            return reserva;

        } catch (Exception e) {
            throw new ReservaInvalidaException("Erro ao criar reserva: " + e.getMessage(), e);
        }
    }
}