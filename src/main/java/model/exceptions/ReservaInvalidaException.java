package model.exceptions;

public class ReservaInvalidaException extends Exception {
    public ReservaInvalidaException(String message) {
        super(message);
    }
    public ReservaInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
