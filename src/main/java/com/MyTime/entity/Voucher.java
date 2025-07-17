package com.MyTime.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString
@Getter
@Setter
@Entity
@Table(name = "voucher_company")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VOUCHER_ID", unique = true, nullable = false)
    private long voucherId;
    @Column(name="VOUCHERNUMBER", length=254)
    private String voucherNumber;
    private long companyId;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;
    @Column(name="DESCRIPTION", length=254)
    private String description;
    private long unit;
    private long unitPrice;
    private long amount;
    private long tax;
    private long totalAmount;
    private String status;

    @Column(name="PAYMENTLINK", length=254)

    private String paymentLink;


}
