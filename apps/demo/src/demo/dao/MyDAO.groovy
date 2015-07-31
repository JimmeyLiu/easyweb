package demo.dao

import demo.model.Hello
import org.easyweb.orm.BaseDAO
import org.easyweb.orm.nutz.NutzDAO

/**
 * Created by jimmey on 15-7-31.
 */
@NutzDAO(dataSource = "local", name = "myDAO")
class MyDAO extends BaseDAO<Hello> {

}
