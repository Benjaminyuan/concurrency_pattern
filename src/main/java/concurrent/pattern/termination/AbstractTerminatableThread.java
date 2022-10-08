package concurrent.pattern.termination;

public abstract class AbstractTerminatableThread extends Thread implements Terminatable {
    private final TerminateToken terminateToken;

    public AbstractTerminatableThread() {
        this(new TerminateToken());
    }
    public AbstractTerminatableThread(TerminateToken token) {
        terminateToken = token;
    }   
    
    public abstract void doCleanUp(Exception cause) throws Exception;
    public abstract void doRun() throws Exception;

    protected void doTerminate() {}
    @Override
    public void run() {
        Exception exception = null;
        try {
            while(true) {
                if(terminateToken.isShutdown() && terminateToken.revervations.get() <= 0) {
                    break;
                } 
                doRun();
            }
        } catch(Exception e) {
            exception = e;
        } finally {
            try {
                doCleanUp(exception);
            } catch(Exception e) {
                terminateToken.notifyTermination(this);
            }
        }
        
    }
    @Override
    public void terminate() {
        terminateToken.requestTerminate();
        try {
            doTerminate();
        } finally {
       
        }
    }
}
