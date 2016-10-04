package dpc.submits;

import dpc.std.Controller;
import dpc.std.models.StdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 10/2/2016.
 */

@RestController
@RequestMapping("/api")
public class SubmissionController extends Controller {

    @Autowired
    private SubmissionService submissionService;

    @RequestMapping(value = "/submit/{contestId}/{problemNumber}",
            method = RequestMethod.POST,
            consumes = "text/plain")
    @ResponseBody
    public ResponseEntity submit(@PathVariable(value = "contestId") String contestId,
                                 @PathVariable(value = "problemNumber") int problemNumber,
                                 final HttpServletRequest req,
                                 @RequestBody final String body) {
        StdRequest stdRequest = pre(req);
        return wrap(submissionService.submit(stdRequest, contestId, problemNumber, body));
    }

    @RequestMapping(value = "/submissions/{contestId}",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getSubmissions(@PathVariable(value = "contestId") String contestId, final HttpServletRequest req) {
        StdRequest stdRequest = pre(req);
        return wrap(submissionService.getSubmissions(stdRequest, contestId));
    }
}
