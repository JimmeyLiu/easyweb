package org.easyweb.app.change;

import org.easyweb.app.App;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 下午9:39
 * <p/>
 * 以app为维度的缓存清理，在
 */
public interface AppChangeListener {

    /**
     * 监听器初始化方法。在初始化的时候会被调用
     */
    public void init();

    /**
     * 停止一个应用，和下面的remove是一个意思，直接去掉
     *
     * @param app
     */
    public void stop(App app);

    /**
     * 应用部署成功
     *
     * @param app
     */
    public void success(App app);

    /**
     * 应用部署失败
     *
     * @param app
     */
    public void failed(App app);

    /**
     * 打印出当前容器中记录的app信息
     *
     * @param app
     * @return
     */
    public String report(App app);

}
