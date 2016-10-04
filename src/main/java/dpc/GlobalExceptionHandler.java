package dpc;
/**
 * Created by jiaweizhang on 9/14/2016.
 */

import dpc.exceptions.*;
import dpc.std.Controller;
import dpc.std.models.StdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends Controller {

    @ExceptionHandler(JwtAuthException.class)
    public ResponseEntity handleJwtAuthException() {
        return wrap(new StdResponse(403, false, "Jwt Auth failed"));
    }

    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity handleNotAdminException() {
        return wrap(new StdResponse(403, false, "You are not an admin"));
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity handleSqlException(Exception e) {
        return wrap(new StdResponse(500, false, "Database error: " + e.getLocalizedMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(Exception e) {
        return wrap(new StdResponse(200, false, "Invalid request: " + e.getLocalizedMessage()));
    }

    @ExceptionHandler(GroupFullException.class)
    public ResponseEntity handleGroupFullException(Exception e) {
        return wrap(new StdResponse(200, false, "Group is full"));
    }

    @ExceptionHandler(NotGroupMemberException.class)
    public ResponseEntity handleNotGroupMemberException(Exception e) {
        return wrap(new StdResponse(200, false, "Not group member"));
    }

    @ExceptionHandler(AlreadyInGroupException.class)
    public ResponseEntity handleAlreadyInGroupException(Exception e) {
        return wrap(new StdResponse(200, false, "You are already in the group"));
    }

    @ExceptionHandler(ContestNotStartedException.class)
    public ResponseEntity handleContestNotStartedException(Exception e) {
        return wrap(new StdResponse(403, false, "Contest has not started yet"));
    }

    @ExceptionHandler(ContestHasEndedException.class)
    public ResponseEntity handleContestHasEndedException(Exception e) {
        return wrap(new StdResponse(403, false, "Contest has ended"));
    }

    @ExceptionHandler(PropertyLoaderException.class)
    public ResponseEntity handlePropertyLoaderException(Exception e) {
        return wrap(new StdResponse(500, false, "Error while retrieving property"));
    }

    @ExceptionHandler(FileReaderException.class)
    public ResponseEntity handleFileReaderException(Exception e) {
        return wrap(new StdResponse(500, false, "Error while reading file"));
    }

    @ExceptionHandler(AlreadyHaveCorrectSubmissionException.class)
    public ResponseEntity handleAlreadyHaveCorrectSubmissionException(Exception e) {
        return wrap(new StdResponse(200, false, "You have already submitted the correct answer"));
    }
}