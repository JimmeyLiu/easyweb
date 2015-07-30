package org.easyweb.app.deploy;

import org.easyweb.app.monitor.ScanResult;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午3:26
 */
public interface DeployListener {

    public void process(ScanResult result) throws DeployException;

}
