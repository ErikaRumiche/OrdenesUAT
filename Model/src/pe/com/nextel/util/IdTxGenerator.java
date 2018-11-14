package pe.com.nextel.util;

/**
 * Created by montoymi on 16/02/2016.
 */
public class IdTxGenerator {
    private static final ThreadLocal<String> context = new ThreadLocal<String>();

    public static void startTransaction() {
        String idTx = String.valueOf(System.currentTimeMillis());
        context.set(idTx);
    }

    public static String getTransactionId() {
        return context.get();
    }

    public static void endTransaction() {
        context.remove();
    }
}
