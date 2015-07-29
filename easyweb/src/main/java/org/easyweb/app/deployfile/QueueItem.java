package org.easyweb.app.deployfile;

import org.easyweb.app.DeployStatus;
import org.easyweb.app.DeployType;
import org.easyweb.app.DeployStatus;
import org.easyweb.app.DeployType;
import org.apache.commons.lang.StringUtils;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:29
 */
public class QueueItem extends Base {

    DeployType deployType;

    DeployStatus status;

    long putQueueTime;

    public DeployType getDeployType() {
        return deployType;
    }

    public void setDeployType(DeployType deployType) {
        this.deployType = deployType;
    }

    public DeployStatus getStatus() {
        return status;
    }

    public void setStatus(DeployStatus status) {
        this.status = status;
    }

    public long getPutQueueTime() {
        return putQueueTime;
    }

    public void setPutQueueTime(long putQueueTime) {
        this.putQueueTime = putQueueTime;
    }

    public static QueueItem parse(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        String[] v = content.split(SPLIT);
        if (v.length < 6) {
            return null;
        }
        try {
            QueueItem item = new QueueItem();
            item.setAppName(v[0]);
            item.setFile(v[2]);
            item.setMd5(v[3]);
            item.setPutQueueTime(Long.valueOf(v[4]));
            item.setDeployType(DeployType.parse(v[5]));
            if (v.length == 7) {
                item.setStatus(DeployStatus.parse(Integer.valueOf(v[6])));
            }
            return item;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(",").append(putQueueTime).append(",");
        sb.append(deployType.getCode());
        if (status != null) {
            sb.append(",").append(status.getStatus());
        }
        return sb.toString();
    }
}
