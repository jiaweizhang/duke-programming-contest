package dpc;
/**
 * Created by jiaweizhang on 9/14/2016.
 */

import dpc.controllers.Controller;
import dpc.exceptions.JwtAuthException;
import dpc.models.responses.StdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends Controller {

    @ExceptionHandler(JwtAuthException.class)
    public ResponseEntity handleError() {
        return wrap(new StdResponse(403, false, "Jwt Auth failed"));
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity handleSqlException(Exception e) {
        return wrap(new StdResponse(500, false, "Database error: " + e.getLocalizedMessage()));
    }
}