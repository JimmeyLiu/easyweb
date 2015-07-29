package org.easyweb.request.error;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午7:42
 */
public enum ErrorType {

    PAGE_NOT_FOUND,//页面找不到
    RENDER_EXCEPTION,//渲染异常
    PIPELINE_ERROR,//pipeline valve执行异常
    APP_NOT_EXIST,//应用不存在
    APP_STATUS_ERROR,//应用状态异常

}
