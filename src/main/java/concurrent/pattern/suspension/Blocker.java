package concurrent.pattern.suspension;

import java.util.concurrent.Callable;

public interface Blocker {
    public <V> V callGuardAction(GuardAction<V> guardAction) throws Exception;

    public void signal() throws InterruptedException;

    public void signalAfter(Callable<Boolean> stateOperation) throws Exception;

    public void broadcastAfter(Callable<Boolean> stateOperation) throws Exception;
}
