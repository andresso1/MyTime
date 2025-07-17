package com.MyTime.entity;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID", unique = true, nullable = false)
    private Integer companyId;
    @Column(name = "NAMECOMPANY", nullable = false)
    private String nameCompany;
    private String address;
    private String phone;
    private String nit;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    private String esTrial;


    public Company() {
    }

    public Company(String nameCompany, String address, String phone, String nit) {
        this.nameCompany = nameCompany;
        this.address = address;
        this.phone = phone;
        this.nit = nit;
        this.dateCreation = new Date();
        this.dateExpiration = new Date();
        this.esTrial = "T";
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    @Override
    public String toString() {
        //= phone;
        //this.nit = nit;
        //this.dateCreation = new Date();
        //this.dateExpiration = new Date();
        //this.esTrial = "T";
        return "Order [orderId=" + this.nameCompany + ", desc=" + this.address + ", value=" + this.phone + "]";
    }
}
