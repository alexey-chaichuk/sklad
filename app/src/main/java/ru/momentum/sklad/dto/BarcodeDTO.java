package ru.momentum.sklad.dto;

import java.io.Serializable;

/**
 * Created by chaichukau on 30.01.18.
 */

public class BarcodeDTO implements Serializable {
    private String barcode;
    private String ownerCode;

    public BarcodeDTO(String barcode) {
        this.barcode = barcode;
    }

    public BarcodeDTO() {
    }

    public BarcodeDTO(String _barcode, String _code) {
        barcode = _barcode;
        ownerCode = _code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
}
