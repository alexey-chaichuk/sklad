package ru.momentum.sklad.dto;

import java.io.Serializable;

/**
 * Created by chaichukau on 30.01.18.
 */

public class BarcodeSearchRespDTO implements Serializable {

    private String owner;
    private String ownerName;
    private String characteristic;
    private String serial;
    private String quality;

    public BarcodeSearchRespDTO() {
    }

    public BarcodeSearchRespDTO(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
