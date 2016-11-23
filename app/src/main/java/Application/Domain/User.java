package Application.Domain;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -6365743330089212031L;
    //private static final long serialVersionUID = 1L;

    private String id = "fake";
    private String pwd = "fake";
    private String name = "fake";
    private String phone = "fake";
    private boolean register = false;
    private String type = null;

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}