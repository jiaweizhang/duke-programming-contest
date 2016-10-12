package dpc.auth.models;

import dpc.std.models.StdRequest;

/**
 * Created by jiaweizhang on 10/12/2016.
 */
public class RegisterRequest extends StdRequest {
    public String email;
    public String name;
    public String password;
    public String school;
    public String classInSchool;
}
