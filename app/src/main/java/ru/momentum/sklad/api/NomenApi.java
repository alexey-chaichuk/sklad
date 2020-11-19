package ru.momentum.sklad.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import ru.momentum.sklad.dto.BarcodeDTO;
import ru.momentum.sklad.dto.Doc1c;
import ru.momentum.sklad.dto.NomenklaturaDTO;
import ru.momentum.sklad.dto.UploadImageResponse;

/**
 * Created by chaichukau on 02.02.18.
 */

public interface NomenApi {
    @GET("first/hs/restapi1/FindByName")
    Call<List<NomenklaturaDTO>> getData(@Query("Name") String searchName);

    @PUT("first/hs/restapi1/AddCode")
    Call<String> AddCode(@Body BarcodeDTO code);

    @Multipart
    @POST("first/hs/restapi1/AddPhoto")
    Call<String> AddPhoto(@Part MultipartBody.Part image, @Part MultipartBody.Part desc);

    @Multipart
    @POST("first/hs/restapi1/UploadImage")
    Call<UploadImageResponse> uploadImage(@Part MultipartBody.Part image);

    @GET("first/hs/restapi1/FindPTU")
    Call<Doc1c> findPTU();

    @GET("/first/hs/restapi/GetPhoto")
    Call<ResponseBody> getPhoto();

}
