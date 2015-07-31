package web

import model.Hello
import org.easyweb.annocation.Page
import org.easyweb.annocation.RequestBean
import org.easyweb.container.BeanService
import org.easyweb.context.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by jimmey on 15-7-30.
 */
class Index {

    def vm
    BeanService beanService;

    Logger logger = LoggerFactory.getLogger("hello")

    @Page(url = "/demo/index")
    def page(@RequestBean Hello hello, Context context) {
        context.putContext("hello", hello)
        context.putContext("say", beanService.say())
        return vm.render("index.vm")
    }

}
