package org.easyweb.orm.nutz;

import groovy.lang.GroovyObject;
import org.easyweb.app.App;
import org.easyweb.bean.BeanFactory;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.orm.datasource.DataSourceFactory;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import javax.sql.DataSource;
import java.io.File;
import java.lang.annotation.Annotation;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 下午2:08
 */
public class NutzDAOParser extends AnnotationParser<NutzDAO> {

    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof NutzDAO;
    }

    @Override
    public void parse(App app, NutzDAO nutzDAO, File file, Object target, GroovyObject groovyObject) {
        DataSource dataSource = DataSourceFactory.getDataSource(app.getName(), nutzDAO.dataSource());
        if (dataSource == null) {
            throw new RuntimeException("dataSource " + nutzDAO.dataSource() + " not prepared");
        }

        Dao impl = new NutDao(dataSource);
        try {
            groovyObject.setProperty("dao", impl);
            groovyObject.invokeMethod("init", null);
        } catch (Exception e) {
            throw new RuntimeException("dataSource " + nutzDAO.dataSource() + " inject error");
        }
        BeanFactory.register(app, nutzDAO.name(), groovyObject);
    }
}
