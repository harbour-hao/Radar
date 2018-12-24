package csp.jndx.com.track;

import java.io.Serializable;

/**
存储个人信息
 */
public class person implements Serializable {
    private String name;
    private double v;//=22.2830;//经度
    private double v1;//=113.561320;//纬度
    private boolean status;//敌友
    private String phone;
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public double getV(){
        return this.v;
    }

    public void setV(double v){
        this.v = v;
    }

    public double getV1(){
        return this.v1;
    }

    public void setV1(double v1){
        this.v1 = v1;
    }

    public boolean getStatus(){
        return this.status;
    }

    public void setStatus( boolean status){
        this.status = status;
    }
}
