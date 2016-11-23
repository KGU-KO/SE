package Application.Domain;

import java.io.Serializable;

public class PaymentInf implements Serializable {

    private static final long serialVersionUID = 2L;
    private String id;
    private String banktype;
    private String account;
    private String type;

    public String getId(){
        return id;
    }

    public String getBanktype(){
        return banktype;
    }

    public String getAccount(){
        return account;
    }

    public String getType(){
        return type;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setBanktype(String banktype){
        this.banktype = banktype;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public void setType(String type){
        this.type = type;
    }
}