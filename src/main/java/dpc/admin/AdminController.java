package dpc.admin;

import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by jiaweizhang on 9/14/2016.
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController extends Controller {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/dbup",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity dbup() throws IOException {
        return wrap(adminService.upgradeDb());
    }

    @RequestMapping(value = "/token/{netId}",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getToken(@PathVariable(value = "netId") String netId) {
        return wrap(adminService.getToken(netId));
    }

}