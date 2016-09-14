package dpc.controllers;

import dpc.exceptions.JwtAuthException;
import dpc.models.responses.StdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 7/26/2016.
 */

@RestController
@RequestMapping("/api/test")
public class TestController extends Controller {

    @Autowired
    JdbcTemplate jt;

    @RequestMapping(value = "/get",
            method = RequestMethod.GET)
    @ResponseBody
    public StdResponse register(final HttpServletRequest request) {
        if (1 == 1) throw new JwtAuthException();
        if (1 == 1) jt.execute("INSERT INTO badTable VALUES (1, 2)");
        return new StdResponse(200, false, "Successful get");
    }

}
