package dpc.cache;

import dpc.cache.models.EmailRequest;
import dpc.cache.models.GroupNameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiaweizhang on 9/28/2016.
 */

@RequestMapping("/api/quickfetch")
@RestController
public class CacheController extends dpc.std.Controller {

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/email",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity checkEmail(@RequestBody final EmailRequest req) {
        return wrap(cacheService.checkEmail(req));
    }

    @RequestMapping(value = "/groupName",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity checkGroupName(@RequestBody final GroupNameRequest req) {
        return wrap(cacheService.checkGroupName(req));
    }
}
