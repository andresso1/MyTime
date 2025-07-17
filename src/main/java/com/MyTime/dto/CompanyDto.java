package com.MyTime.dto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Date;

public class CompanyDto {
    private String nameCompany;
    private String address;
    private String phone;
    private String nit;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    private String esTrial;

    public CompanyDto() {
    }

    public CompanyDto(String nameCompany, String address, String phone, String nit, Date dateCreation, Date dateExpiration, String esTrial) {
        this.nameCompany = nameCompany;
        this.address = address;
        this.phone = phone;
        this.nit = nit;
        this.dateCreation = dateCreation;
        this.dateExpiration = dateExpiration;
        this.esTrial = esTrial;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getEsTrial() {
        return esTrial;
    }

    public void setEsTrial(String esTrial) {
        this.esTrial = esTrial;
    }
}
