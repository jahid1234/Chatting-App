package com.example.chatappdemo.Fragment;

import com.example.chatappdemo.Notifications.MyResponse;
import com.example.chatappdemo.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkdbOEz8:APA91bGjTJmUQIwOd6Bj7AjaYb5MDcczqXkRmMoMXwgF17g2w7Jil3lTcHc7zJxwXqWNOR1CkpRX9NsEJPg0nvyl7PYHAxdeWm62TBc-PKfPXGcior_Lrq4GTOiPf8ugu_pQyjfnzRJC"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
