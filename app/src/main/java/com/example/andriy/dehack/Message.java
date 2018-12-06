package com.example.andriy.dehack;

/**
 * Created by Andriy on 17.02.2018.
 */

public class Message{
    int id;
    String doc_number;                ;
    String doc_date ;
    String doc_v_date  ;
    String trans_date  ;
    String amount  ;
    String payer_edrpou   ;
    String payer_name  ;
    String payer_account ;
    String payer_mfo  ;
    String payer_bank;
    String recipt_edrpou  ;
    String recipt_name ;
    String recipt_account;
    String recipt_bank ;
    String recipt_mfo ;
    String payment_details;
    String doc_add_attr  ;
    int    region_id  ;

    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public String getDoc_number() {
        return doc_number;
    }

    public int getRegion_id() {
        return region_id;
    }

    public String getDoc_v_date() {
        return doc_v_date;
    }

    public String getPayer_account() {
        return payer_account;
    }

    public String getPayer_bank() {
        return payer_bank;
    }

    public String getDoc_add_attr() {
        return doc_add_attr;
    }

    public String getPayer_edrpou() {
        return payer_edrpou;
    }

    public String getPayer_mfo() {
        return payer_mfo;
    }

    public String getPayer_name() {
        return payer_name;
    }

    public String getPayment_details() {
        return payment_details;
    }

    public String getRecipt_account() {
        return recipt_account;
    }

    public String getRecipt_bank() {
        return recipt_bank;
    }

    public String getRecipt_edrpou() {
        return recipt_edrpou;
    }

    public String getRecipt_mfo() {
        return recipt_mfo;
    }

    public String getRecipt_name() {
        return recipt_name;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }

    public void setDoc_number(String doc_number) {
        this.doc_number = doc_number;
    }

    public void setDoc_v_date(String doc_v_date) {
        this.doc_v_date = doc_v_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoc_add_attr(String doc_add_attr) {
        this.doc_add_attr = doc_add_attr;
    }

    public void setPayer_account(String payer_account) {
        this.payer_account = payer_account;
    }

    public void setPayer_bank(String payer_bank) {
        this.payer_bank = payer_bank;
    }

    public void setPayer_edrpou(String payer_edrpou) {
        this.payer_edrpou = payer_edrpou;
    }

    public void setPayer_mfo(String payer_mfo) {
        this.payer_mfo = payer_mfo;
    }

    public void setPayer_name(String payer_name) {
        this.payer_name = payer_name;
    }

    public void setPayment_details(String payment_details) {
        this.payment_details = payment_details;
    }

    public void setRecipt_account(String recipt_account) {
        this.recipt_account = recipt_account;
    }

    public void setRecipt_bank(String recipt_bank) {
        this.recipt_bank = recipt_bank;
    }

    public void setRecipt_edrpou(String recipt_edrpou) {
        this.recipt_edrpou = recipt_edrpou;
    }

    public void setRecipt_mfo(String recipt_mfo) {
        this.recipt_mfo = recipt_mfo;
    }

    public void setRecipt_name(String recipt_name) {
        this.recipt_name = recipt_name;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
    }
}
