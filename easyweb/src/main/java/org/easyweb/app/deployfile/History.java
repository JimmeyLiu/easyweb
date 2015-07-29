package org.easyweb.app.deployfile;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 下午1:59
 */
public class History {

    private QueueItem item;

    private long startTime;

    public History(QueueItem item, long startTime) {
        this.item = item;
        this.startTime = startTime;
    }

    public QueueItem getItem() {
        return item;
    }

    public void setItem(QueueItem item) {
        this.item = item;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public static History parse(String line) {
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(item.toString());
        sb.append(Base.SPLIT).append(startTime);
        sb.append(Base.SPLIT).append(System.currentTimeMillis());
        return sb.toString();
    }
}
