package publish.web

import org.easyweb.annocation.Page
import org.easyweb.velocity.Vm

/**
 * Created by jimmey on 15-7-31.
 */
class Index {

    Vm vm

    @Page(url = "/publish/index")
    def a() {
        return vm.render("index.vm")
    }

}
