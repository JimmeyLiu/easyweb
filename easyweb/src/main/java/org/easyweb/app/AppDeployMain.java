package org.easyweb.app;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.easyweb.Configuration;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.deployfile.DeployInfoFile;
import org.easyweb.app.deployfile.DeployQueueFile;
import org.easyweb.app.deployfile.QueueItem;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.easyweb.util.EasywebLogger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午3:06
 */

@Component
public class AppDeployMain implements ApplicationListener {
    @Resource
    GroovyObjectLoader groovyObjectLoader;

    private volatile boolean inited = false;

    @Resource
    private AppDeployer appDeployer;

    Thread scanThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
                try {
                    deploy(null);
                    Thread.sleep(10000);
                } catch (Throwable e) {
                    EasywebLogger.error("deploy error", e);
                }
            }
        }
    });


    /**
     * 应用启动后，需要将之前部署正常的和在部署队列的应用重新部署。
     * deploy_queue文件中的应用优先级高于deploy_info
     */
    private void startup() {
        List<QueueItem> list = DeployQueueFile.getQueue();
        List<QueueItem> infoList = DeployInfoFile.getInfoList();
        for (QueueItem info : infoList) {
            boolean contain = false;
            for (QueueItem item : list) {
                if (item.getAppName().equals(info.getAppName())) {
                    contain = true;
                }
            }
            if (!contain) {
                QueueItem item = new QueueItem();
                item.setAppName(info.getAppName());
                item.setFile(info.getFile());
                item.setMd5(info.getMd5());
                //如果是回滚类型的发布，则重置为正常发布，否则直接从info中copy信息
                if (DeployType.ROLLBACK.equals(info.getDeployType())) {
                    item.setDeployType(DeployType.COMMON);
                } else {
                    item.setDeployType(info.getDeployType());
                }
                item.setStatus(DeployStatus.START);
                list.add(item);
            }
        }

        //重置所有任务为开始状态
        for (QueueItem item : list) {
            item.setStatus(DeployStatus.START);
        }
        deploy(list);
    }

    @SuppressWarnings("unchecked")
    public void deploy(List<QueueItem> list) {
        if (list == null) {
            list = DeployQueueFile.getQueue();
        }

        if (list.isEmpty()) {
            return;
        }

        for (QueueItem item : list) {
            if (!DeployStatus.START.equals(item.getStatus())) {
                continue;
            }
            DeployQueueFile.update(item);
            AppDeployThread thread = new AppDeployThread(appDeployer, groovyObjectLoader, item);

            Thread t = new Thread(thread);
            t.setName("Deploy" + item.getAppName());
            t.start();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (!Configuration.isDev() && !inited) {
            startup();
            scanThread.setName("Easyweb-Queue-Thread");
            scanThread.start();
            inited = true;
        }


    }

    public static void main(String[] args) {
        try {
            FileAlterationObserver observer = new FileAlterationObserver("/Users/jimmey/workspace/platform/easyweb");
            observer.addListener(new FileAlterationListenerAdaptor() {
                @Override
                public void onFileCreate(File file) {
                    System.out.println("Create " + file.getAbsolutePath());
                }

                @Override
                public void onFileChange(File file) {
                    System.out.println("Change " + file.getAbsolutePath());
                }

                @Override
                public void onFileDelete(File file) {
                    System.out.println("Delete " + file.getAbsolutePath());
                }
            });
            FileAlterationMonitor monitor = new FileAlterationMonitor();
            monitor.addObserver(observer);

            monitor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
