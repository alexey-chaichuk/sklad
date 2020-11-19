package ru.momentum.sklad.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Doc1c implements Serializable {

    @SerializedName("#type")
    @Expose
    String type;

    @SerializedName("#value")
    @Expose
    Doc1cVaue value;

    public class Doc1cVaue {
        @SerializedName("СуммаДокумента")
        @Expose
        String sum;

        @SerializedName("Товары")
        @Expose
        ArrayList<Doc1cGoods> goods;

    }

    public class Doc1cGoods {
        @SerializedName("Номенклатура")
        @Expose
        String nomen;

        @SerializedName("Количество")
        @Expose
        String kolvo;

        @SerializedName("Штрихкод")
        @Expose
        String barcode;

        @SerializedName("Артикул")
        @Expose
        String article;

        @SerializedName("Код")
        @Expose
        String code;

        @Override
        public String toString() {
            return "Doc1cGoods{" +
                    "nomen='" + nomen + '\'' +
                    ", kolvo='" + kolvo + '\'' +
                    ", barcode='" + barcode + '\'' +
                    ", article='" + article + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }

        public String getNomen() {
            return nomen;
        }

        public String getKolvo() {
            return kolvo;
        }

        public String getBarcode() {
            return barcode;
        }

        public String getArticle() {
            return article;
        }

        public String getCode() {
            return code;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
    }

    public Doc1cVaue getValue() {
        return value;
    }

    public ArrayList<Doc1cGoods> getGoods() {
        return value.goods;
    }

    @Override
    public String toString() {
        String val =  "Doc1c{" +
                "type='" + type + '\'' +
                ", value=" + value +
                "{sum='" + value.sum + "'";
        for (Doc1cGoods good: value.goods) {
            val += "[" + good.nomen + " -> " + good.kolvo + " -> (" + good.barcode + ")]";
        }

        val = val + "}}";
        return val;
    }
}
