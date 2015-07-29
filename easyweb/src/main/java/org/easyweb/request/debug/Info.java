package org.easyweb.request.debug;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午9:26
 */
public class Info {

    /**
     * 内容标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 输出类型：
     * 0：直接文本输出
     * 1：pre标签输出
     * 2：pre隐藏输出
     */
    private int type = 0;
    /**
     * pre的高度
     */
    private int height = 200;

    public Info() {
    }

    public Info(String title) {
        this.title = title;
    }

    public Info(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Info(String title, String content, int type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Info(String title, String content, int type, int height) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
