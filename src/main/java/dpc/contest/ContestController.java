package dpc.contest;

import dpc.contest.models.ContestCreationRequest;
import dpc.exceptions.NotAdminException;
import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 9/17/2016.
 */

@RestController
@RequestMapping("/api/contests")
public class ContestController extends Controller {

    @Autowired
    private ContestService contestService;

    /**
     * Fetch all contests
     *
     * @return list of contests
     */
    @RequestMapping(value = "",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getContests() {

        // return contests
        return wrap(contestService.getContests());
    }

    /**
     * Fetch contest by name
     *
     * @param contestId
     * @return contest
     */
    @RequestMapping(value = "/{contest}",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getToken(@PathVariable(value = "contest") String contestId) {
        // return contest
        return wrap(contestService.getContest(contestId));
    }

    /**
     * Create new contest
     *
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    @ResponseBody
    public ResponseEntity createContest(@RequestBody final ContestCreationRequest req, final HttpServletRequest request) {
        // validate admin
        pre(req, request);
        if (!isAdmin(req)) {
            throw new NotAdminException();
        }
        return wrap(contestService.createContest(req));
    }
}