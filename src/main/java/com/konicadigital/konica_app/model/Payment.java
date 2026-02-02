package com.konicadigital.konica_app.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payment_id;

    private String paymentMethod;
    private String transactionCode;
    private String paymentStatus;
    private LocalDateTime paymentDate = LocalDateTime.now();
    private String paymentUrl;
    private String pidxCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    public Payment(int payment_id, String paymentMethod, String transactionCode, String paymentStatus, LocalDateTime paymentDate, String paymentUrl, String pidxCode) {
        this.payment_id = payment_id;
        this.paymentMethod = paymentMethod;
        this.transactionCode = transactionCode;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.paymentUrl = paymentUrl;
        this.pidxCode = pidxCode;
    }

    public Payment(){

    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPidxCode() {
        return pidxCode;
    }

    public void setPidxCode(String pidxCode) {
        this.pidxCode = pidxCode;
    }
}
