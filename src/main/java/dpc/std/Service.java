package dpc.std;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by jiaweizhang on 9/14/2016.
 */

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    protected
    JdbcTemplate jt;
}
