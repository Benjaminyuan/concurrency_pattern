package concurrent.pattern.suspension;

import java.util.concurrent.Callable;

public abstract class GuardAction<V> implements Callable<V> {
    protected final Pridicate pridicate;
    public GuardAction(Pridicate pridicate) {
        this.pridicate = pridicate;
    }
}
