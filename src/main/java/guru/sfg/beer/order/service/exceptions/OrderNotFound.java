package guru.sfg.beer.order.service.exceptions;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(String message) {
        super(message);
    }

    public OrderNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
