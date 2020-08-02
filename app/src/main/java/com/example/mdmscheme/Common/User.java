package com.example.mdmscheme.Common;

public class User {

    private String fullname;
    private String email;
    private String password;
    private String schoolid;
    private String schoolname;

    public  User() {}

    public User(String fullName, String email) {
        this.fullname = fullName;
        this.email = email;
    }

    public User(String fullName, String email, String schoolname,
                String schoolid ) {
        this.email = email;
        this.fullname = fullName;
        this.schoolid = schoolid;
        this.schoolname = schoolname;
    }


    public void setpassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
    public  void  setEmail(String email){
        this.email = email;
    }

    public String getFullName() {
        return fullname;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
