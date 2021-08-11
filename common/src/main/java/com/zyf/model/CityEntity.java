package com.zyf.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CityEntity {

    /*城市编码*/
    @Id
    private String Code;
    /*城市名称*/
    private String Name;
    /*所属省份*/
    private String Province;
    /*省份经度*/
    private double Longitude;
    /*省份纬度*/
    private double Latitude;
    /*是否是热点城市*/
    private int Hot;
    /*首字母拼写*/
    private String FirstLetter;
    @Generated(hash = 950982108)
    public CityEntity(String Code, String Name, String Province, double Longitude,
            double Latitude, int Hot, String FirstLetter) {
        this.Code = Code;
        this.Name = Name;
        this.Province = Province;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.Hot = Hot;
        this.FirstLetter = FirstLetter;
    }
    @Generated(hash = 2001321047)
    public CityEntity() {
    }
    public String getCode() {
        return this.Code;
    }
    public void setCode(String Code) {
        this.Code = Code;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getProvince() {
        return this.Province;
    }
    public void setProvince(String Province) {
        this.Province = Province;
    }
    public double getLongitude() {
        return this.Longitude;
    }
    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }
    public double getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }
    public int getHot() {
        return this.Hot;
    }
    public void setHot(int Hot) {
        this.Hot = Hot;
    }
    public String getFirstLetter() {
        return this.FirstLetter;
    }
    public void setFirstLetter(String FirstLetter) {
        this.FirstLetter = FirstLetter;
    }

}
