package dpc.groups;

import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.Controller;
import dpc.std.models.StdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 9/28/2016.
 */

@RestController
@RequestMapping("/api/groups")
public class GroupController extends Controller {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/create/{contestId}",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity createGroup(@PathVariable(value = "contestId") String contestId, @RequestBody final CreateGroupRequest req, final HttpServletRequest request) {
        pre(req, request);
        return wrap(groupService.createGroup(req, contestId));
    }

    @RequestMapping(value = "/join/{contestId}",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity joinGroup(@PathVariable(value = "contestId") String contestId, @RequestBody final JoinGroupRequest req, final HttpServletRequest request) {
        pre(req, request);
        return wrap(groupService.joinGroup(req, contestId));
    }

    @RequestMapping(value = "/delete/{contestId}",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    public ResponseEntity leaveGroup(@PathVariable(value = "contestId") String contestId, final HttpServletRequest request) {
        StdRequest stdRequest = pre(request);
        return wrap(groupService.leaveGroup(stdRequest, contestId));
    }

    @RequestMapping(value = "/info/{contestId}",
            method = RequestMethod.GET)
    public ResponseEntity infoGroup(@PathVariable(value = "contestId") String contestId, final HttpServletRequest request) {
        StdRequest stdRequest = pre(request);
        return wrap(groupService.infoGroup(stdRequest, contestId));
    }
}
