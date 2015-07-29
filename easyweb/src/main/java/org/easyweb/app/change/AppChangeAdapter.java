package org.easyweb.app.change;

import org.easyweb.app.App;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午10:07
 * <p/>
 * listener的适配器，都是默认实现
 */
public class AppChangeAdapter implements AppChangeListener {


    @Override
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop(App app) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void success(App app) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void failed(App app) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String report(App app) {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
