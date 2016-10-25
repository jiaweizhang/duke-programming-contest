package dpc.admin;

import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by jiaweizhang on 9/14/2016.
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController extends Controller {

    @Autowired
    AdminService adminService;

    /**
     * Endpoint for resetting database
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/dbup",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity dbup() throws IOException {
        return wrap(adminService.upgradeDb());
    }
}