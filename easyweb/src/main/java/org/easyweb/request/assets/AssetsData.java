package org.easyweb.request.assets;

/**
 * User: jimmey/shantong
 * Date: 13-8-2
 * Time: 下午8:23
 */
public class AssetsData {

    private long lastModified;

    private byte[] bytes;

    public AssetsData(long lastModified, byte[] bytes) {
        this.lastModified = lastModified;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
