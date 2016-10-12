package dpc.auth;

import dpc.auth.models.LoginRequest;
import dpc.auth.models.RegisterRequest;
import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 9/16/2016.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController extends Controller {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    @ResponseBody
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest, final HttpServletRequest httpServletRequest) {
        pre(registerRequest, httpServletRequest);
        return wrap(authService.register(registerRequest));
    }

    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            headers = {"Content-type-application/json"})
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, final HttpServletRequest httpServletRequest) {
        pre(loginRequest, httpServletRequest);
        return wrap(authService.login(loginRequest));
    }

}
