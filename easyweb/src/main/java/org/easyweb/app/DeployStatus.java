package org.easyweb.app;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 上午8:24
 */
public enum DeployStatus {

    START(0, "begin deploy"),
    DEPLOYING(1, "deploying"),
    SUCCESS(2, "deploy success"),
    FAILED(3, "deploy failed");

    int status;
    String msg;

    DeployStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static DeployStatus parse(int status) {
        for (DeployStatus type : values()) {
            if (status == type.status) {
                return type;
            }
        }
        return START;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}
