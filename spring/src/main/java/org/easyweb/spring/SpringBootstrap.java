package org.easyweb.spring;

import org.easyweb.Easyweb;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jimmey on 15-7-30.
 */
public class SpringBootstrap implements ApplicationContextAware, ApplicationListener {

    ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        String[] names = context.getBeanDefinitionNames();
        Map<String, Object> binding = new HashMap<String, Object>(names.length);
        for (int i = 0; i < names.length; i++) {
            binding.put(names[i], context.getBean(names[i]));
        }
        Easyweb.initialize(binding);
    }
}
