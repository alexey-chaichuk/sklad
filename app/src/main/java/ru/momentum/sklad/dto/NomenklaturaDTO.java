package ru.momentum.sklad.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chaichukau on 23.01.18.
 */

public class NomenklaturaDTO implements Serializable, Parcelable {
    private long id;
    private String title;
    private String code;
    private String article;
    private String link;
    private Date remindDate;

    public NomenklaturaDTO(String title) {
        this.title = title;
    }

    public NomenklaturaDTO() {
    }

    protected NomenklaturaDTO(Parcel in) {
        id = in.readLong();
        title = in.readString();
        code = in.readString();
        article = in.readString();
        link = in.readString();
        remindDate = (Date) in.readSerializable();
    }

    public static final Creator<NomenklaturaDTO> CREATOR = new Creator<NomenklaturaDTO>() {
        @Override
        public NomenklaturaDTO createFromParcel(Parcel in) {
            return new NomenklaturaDTO(in);
        }

        @Override
        public NomenklaturaDTO[] newArray(int size) {
            return new NomenklaturaDTO[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(code);
        parcel.writeString(article);
        parcel.writeString(link);
        parcel.writeSerializable(remindDate);
    }
}
