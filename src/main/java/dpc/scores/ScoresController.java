package dpc.scores;

import dpc.std.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiaweizhang on 10/3/2016.
 */

@RestController
@RequestMapping("/api/scoreboard")
public class ScoresController extends Controller {

    @Autowired
    private ScoresService scoresService;

    @RequestMapping(value = "/{contestId}",
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getScoreboard(@PathVariable(value = "contestId") String contestId) {
        return wrap(scoresService.getScoreboard(contestId));
    }
}
