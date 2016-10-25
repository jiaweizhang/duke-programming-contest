package dpc.auth;

import dpc.auth.models.*;
import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiaweizhang on 9/16/2016.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController extends Controller {

    @Autowired
    private AuthService authService;

    /**
     * Register new account
     *
     * @param registerRequest
     * @return auth token
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    @ResponseBody
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        return wrap(authService.register(registerRequest));
    }

    /**
     * Login to account
     *
     * @param loginRequest
     * @return auth token
     */
    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        return wrap(authService.login(loginRequest));
    }

    /**
     * Sends email with token to recover password with
     *
     * @param forgotRequest
     * @return
     */
    @RequestMapping(value = "/forgot",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity forgot(@RequestBody ForgotRequest forgotRequest) {
        return wrap(authService.forgot(forgotRequest));
    }

    /**
     * Validates a forgot-password token
     *
     * @param validateTokenRequest
     * @return
     */
    @RequestMapping(value = "/validateToken",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity validateToken(@RequestBody ValidateTokenRequest validateTokenRequest) {
        return wrap(authService.validateToken(validateTokenRequest));
    }

    /**
     * Resets a password using a forgot-password token
     *
     * @param resetPasswordRequest
     * @return
     */
    @RequestMapping(value = "/resetPassword",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return wrap(authService.resetPassword(resetPasswordRequest));
    }

}
