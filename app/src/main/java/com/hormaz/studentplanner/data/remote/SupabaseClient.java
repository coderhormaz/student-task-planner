package com.hormaz.studentplanner.data.remote;

import com.hormaz.studentplanner.BuildConfig;
import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.remote.api.SupabaseAuthApi;
import com.hormaz.studentplanner.data.remote.api.SupabaseDbApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {

    private final SupabaseAuthApi authApi;
    private final SupabaseDbApi dbApi;

    public SupabaseClient(PreferencesManager preferencesManager) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder()
                                .header("apikey", BuildConfig.SUPABASE_ANON_KEY)
                                .header("Content-Type", "application/json");

                        // Add auth token if available
                        String token = preferencesManager.getAccessToken();
                        if (token != null && !token.isEmpty()) {
                            builder.header("Authorization", "Bearer " + token);
                        } else {
                            builder.header("Authorization", "Bearer " + BuildConfig.SUPABASE_ANON_KEY);
                        }

                        return chain.proceed(builder.build());
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SUPABASE_URL + "/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(SupabaseAuthApi.class);
        dbApi = retrofit.create(SupabaseDbApi.class);
    }

    public SupabaseAuthApi getAuthApi() {
        return authApi;
    }

    public SupabaseDbApi getDbApi() {
        return dbApi;
    }
}
