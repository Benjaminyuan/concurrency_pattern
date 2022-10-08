package concurrent.pattern.termination;

import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminateToken {
    private boolean shutdown = false;
    final AtomicInteger revervations = new AtomicInteger(0);
    private final BlockingQueue<WeakReference<Terminatable>> terminatableThreads;

    public TerminateToken() {
        terminatableThreads = new LinkedBlockingQueue<>();
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void requestTerminate() {
        shutdown = true;
    }

    protected void register(Terminatable thread) {
        terminatableThreads.offer(new WeakReference<Terminatable>(thread));
        revervations.incrementAndGet();
    }

    protected void notifyTermination(Thread thread) {
        WeakReference<Terminatable> otherThreadWR;
        while ((otherThreadWR = terminatableThreads.poll()) != null) {
            Terminatable otherThread = otherThreadWR.get();
            if (otherThread != null) {
                otherThread.terminate();
            }
        }
    }

}
