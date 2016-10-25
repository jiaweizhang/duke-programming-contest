package dpc.statics;

import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiaweizhang on 10/2/2016.
 */

@RestController
@RequestMapping("/api")
public class StaticController extends Controller {

    @Autowired
    private StaticService staticService;

    /**
     * Returns static PDF containing problem
     *
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/problems/{fileName}",
            method = RequestMethod.GET,
            produces = "application/pdf")
    @ResponseBody
    public ResponseEntity getProblem(@PathVariable(value = "fileName") String fileName) {
        return staticService.getProblem(fileName);
    }

    /**
     * Returns static text file containing problem input
     *
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/inputs/{fileName}",
            method = RequestMethod.GET,
            produces = "text/plain")
    @ResponseBody
    public ResponseEntity getInput(@PathVariable(value = "fileName") String fileName) {
        return staticService.getInput(fileName);
    }
}
