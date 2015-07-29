package org.easyweb.app;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:54
 */
public enum DeployType {
    COMMON("common", "正常发布流程"),//正常发布
    TEMPLATE("template", "模板发布，不重启所有"),//web目录发布
    ASSETS("assets", "静态资源发布"),//assets目录发布
    ROLLBACK("rollback", "回滚"),
    STOP("stop", "停止应用"),
    UNKNOWN("unknown", "未知发布");

    String code;
    String msg;

    DeployType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static DeployType parse(String code) {
        for (DeployType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getCode() {
        return code;
    }
}
