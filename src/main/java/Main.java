import java.util.List;

public class Main {
    public static void main(String[] args) {
        HospedeDAO hospedeDAO = new HospedeDAO();
        QuartoDAO quartoDAO = new QuartoDAO();
        ReservaDAO reservaDAO = new ReservaDAO();

        Hospede h1 = new Hospede("45066552812", "Guilherme Damico Balsi", "14991899669");
        Quarto q1 = new QuartoSimples(101, 150.0, 2);
        Quarto q2 = new QuartoLuxo(102, 300.0, 3);

        hospedeDAO.salvar(h1);
        quartoDAO.salvar(q1);
        quartoDAO.salvar(q2);

        System.out.println("Dados inseridos no banco!");

        Hospede hospedeBuscado = hospedeDAO.buscarPorCPF("45066552812", quartoDAO, reservaDAO);
        if (hospedeBuscado != null) {
            System.out.println("\nHóspede encontrado: " + hospedeBuscado.getNome());
        }

        try {
            hospedeBuscado.reservar(q1, "2025-06-01", "2025-06-05");
            Reserva novaReserva = hospedeBuscado.getReservas().get(0);
            reservaDAO.salvar(novaReserva);
            System.out.println("Reserva feita com sucesso!");
        } catch (ReservaInvalidaException e) {
            System.out.println("Erro ao reservar: " + e.getMessage());
        }

        List<Quarto> quartos = quartoDAO.listar();
        System.out.println("\n Lista de Quartos ");
        for (Quarto q : quartos) {
            System.out.println("Quarto " + q.getNumero() + " | Tipo: " + q.getTipo() +
                    " | Disponível: " + q.isDisponivel() + " | Preço: R$" + q.getPrecoPorNoite());
        }

        // Listar reservas do hóspede
        List<Reserva> reservasDoHospede = hospedeBuscado.getReservas();
        System.out.println("\n Reservas do Hóspede ");
        for (Reserva r : reservasDoHospede) {
            System.out.println("Quarto: " + r.getQuarto().getNumero() +
                    " | Entrada: " + r.getDataEntrada() +
                    " | Saída: " + r.getDataSaida());
        }
    }
}
