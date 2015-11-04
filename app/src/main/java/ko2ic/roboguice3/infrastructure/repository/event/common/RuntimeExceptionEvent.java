package ko2ic.roboguice3.infrastructure.repository.event.common;

/**
 * Created by ishii_ko on 2015/11/04.
 */
public class RuntimeExceptionEvent {

    private Throwable t;

    public RuntimeExceptionEvent(Throwable t) {
        this.t = t;
    }

    public Throwable getThrowable() {
        return t;
    }
}
