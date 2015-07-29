package org.easyweb.request.pipeline;

import org.easyweb.context.Context;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 上午11:53
 */
public interface Valve {

    public void invoke(Context context) throws Exception;

}
