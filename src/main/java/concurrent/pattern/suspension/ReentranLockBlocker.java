package concurrent.pattern.suspension;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentranLockBlocker implements Blocker {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    @Override
    public <V> V callGuardAction(GuardAction<V> guardAction) throws Exception {
        lock.lockInterruptibly();
        final Pridicate pridicate = guardAction.pridicate;
        try {
            while (!pridicate.evaluate()) {
                cond.await();
            }
            return guardAction.call();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void signal() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            cond.signal();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void signalAfter(Callable<Boolean> stateOperation) throws Exception {
        lock.lockInterruptibly();
        try {
            if (stateOperation.call()) {
                cond.signal();
            }
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void broadcastAfter(Callable<Boolean> stateOperation) throws Exception {
        lock.lockInterruptibly();
        try {
            if (stateOperation.call()) {
                cond.signalAll();
            }
        } finally {
            lock.unlock();
        }

    }

}
