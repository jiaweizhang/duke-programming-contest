package dpc.user;

import dpc.std.Controller;
import dpc.std.models.StdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 10/27/2016.
 */

@RestController
@RequestMapping("/api/user")
public class UserController extends Controller {

    @Autowired
    private UserService userService;

    /**
     * Fetch user information
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "",
            method = RequestMethod.GET)
    public ResponseEntity userInfo(final HttpServletRequest request) {
        StdRequest stdRequest = pre(request);
        return wrap(userService.userInfo(stdRequest));
    }
}
