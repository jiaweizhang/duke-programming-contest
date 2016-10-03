package dpc.submits;

import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiaweizhang on 10/2/2016.
 */

@RestController
@RequestMapping("/api/submit")
public class SubmissionController extends Controller {

    @Autowired
    private SubmissionService submissionService;

    @RequestMapping(value = "/{contestId}/{problemNumber}",
            method = RequestMethod.POST,
            consumes = "text/plain")
    @ResponseBody
    public ResponseEntity submit(@PathVariable(value = "contestId") String contestId,
                                 @PathVariable(value = "problemNumber") int problemNumber,
                                 final HttpServletRequest req,
                                 final String body) {
        System.out.println(body);
        return ResponseEntity.ok().body(body);
    }
}
