package org.easyweb.app.deploy.process;

import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.deploy.DeployListener;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.deploy.DeployListener;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:51
 */
public abstract class FileProcessor implements DeployListener {

    protected FileProcessor() {
        AppDeployer.addListener(this);
    }

}
