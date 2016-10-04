package dpc.auth;

import dpc.std.Controller;
import dpc.std.models.StdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiaweizhang on 9/16/2016.
 */

@RestController
@RequestMapping("/serveroauth")
public class OAuthController extends Controller {

    @Autowired
    private OAuthService oAuthService;

    @RequestMapping(value = "",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity auth(@RequestParam(value = "access_token", required = false) final String auth_code) {
        if (auth_code == null) {
            return wrap(new StdResponse(200, false, "Invalid authcode"));
        }
        return wrap(oAuthService.auth(auth_code));
    }

}
