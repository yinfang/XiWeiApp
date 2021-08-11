package com.zyf.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ProvinceEntity {
    /*省份编码*/
    @Id
    private String Code;
    /*省份名称*/
    private String Name;
    /*省份全拼*/
    private String Spell;
    /*省份经度*/
    private double Longitude;
    /*省份纬度*/
    private double Latitude;
    @Generated(hash = 520382120)
    public ProvinceEntity(String Code, String Name, String Spell, double Longitude,
            double Latitude) {
        this.Code = Code;
        this.Name = Name;
        this.Spell = Spell;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
    }
    @Generated(hash = 1419486807)
    public ProvinceEntity() {
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
    public String getSpell() {
        return this.Spell;
    }
    public void setSpell(String Spell) {
        this.Spell = Spell;
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

}
