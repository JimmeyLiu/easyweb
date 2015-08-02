package demo.pipeline

import org.easyweb.request.pipeline.Valve

/**
 * Created by jimmey on 15-7-31.
 */
@Valve(order = 2, method = "invoke")
class AuthValve {
    @Override
    void invoke() throws Exception {

    }

}
