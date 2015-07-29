package org.easyweb.app.deploy;

public class DeployException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -240485744100165891L;

    public DeployException() {
    }

    public DeployException(String msg) {
        super(msg);
    }

    public DeployException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
