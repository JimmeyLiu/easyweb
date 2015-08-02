package demo.pipeline

import org.easyweb.request.pipeline.Valve

/**
 * Created by jimmey on 15-7-31.
 */
@Valve(order = 1, method = "invoke")
class MenuValve {

    @Override
    void invoke() throws Exception {
    }


}
