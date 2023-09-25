package com.example.nativeapplication.retrofit;

import static java.security.AccessController.getContext;

import android.util.Log;
import android.widget.Toast;

import com.example.nativeapplication.model.Customer;
import com.example.nativeapplication.model.ServiceProfessional;
import com.example.nativeapplication.model.TimeSlot;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* A singleton class */
public class ApiManager {
    private static ApiService apiService;
    private static ApiManager apiManager;
    private static ServiceprofessionalApi serviceProfessionalApi;

    public interface ApiCallback<T> {
        void onSuccess(T response);
        void onFailure(Throwable t);
    }

    private ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://snap-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        serviceProfessionalApi = retrofit.create(ServiceprofessionalApi.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void getTimeSlotsInRange(final ApiCallback<List<TimeSlot>> callback, String date, Integer serviceProfessionalId) {
        Call<List<TimeSlot>> call = apiService.getFreeTimeSlots(date, serviceProfessionalId);
        call.enqueue(new Callback<List<TimeSlot>>() {
            @Override
            public void onResponse(Call<List<TimeSlot>> call, Response<List<TimeSlot>> response) {
                if (response.isSuccessful()) {
                    List<TimeSlot> timeSlots = response.body();
                    callback.onSuccess(timeSlots);
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<TimeSlot>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getServiceProfessionalFromId(final ApiCallback<ServiceProfessional> callback, Integer id) {
        Call<ServiceProfessional> call = apiService.getServiceProfessionalFromId(id);
        call.enqueue(new Callback<ServiceProfessional>() {
            @Override
            public void onResponse(Call<ServiceProfessional> call, Response<ServiceProfessional> response) {
                if (response.isSuccessful()) {
                    ServiceProfessional serviceProfessionals = response.body();
                    callback.onSuccess(serviceProfessionals);
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ServiceProfessional> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getServiceProfessionalByCategory(final ApiCallback<List<ServiceProfessional>> callback, String subCategory) throws UnsupportedEncodingException {
        Call<List<ServiceProfessional>> call = apiService.getServiceProfessionalFromCategory(subCategory);
        call.enqueue(new Callback<List<ServiceProfessional>>() {
            @Override
            public void onResponse(Call<List<ServiceProfessional>> call, Response<List<ServiceProfessional>> response) {
                if (response.isSuccessful()){
                    List<ServiceProfessional> serviceProfessionals = response.body();
                    Log.d("API onResponse", "Received " + serviceProfessionals.size() + " service professionals");
                    callback.onSuccess(response.body());
                }else{
                    Log.e("API response failed",response.message());
                    callback.onFailure(new Exception(response.message()));
                }
            }
            @Override
            public void onFailure(Call<List<ServiceProfessional>> call, Throwable t) {
                Log.d("API onFailure", t.toString());
                callback.onFailure(t);
            }
        });
    }

    public void saveServiceProfessional(final ApiCallback<Object> callback, ServiceProfessional serviceProfessional) {
        Call<Object> call = serviceProfessionalApi.save(serviceProfessional);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("RESPONSE", response.code() + " ");
                if (response.code() == 200) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    public void createCustomer(final ApiCallback<Customer> callback, Customer customer) {
        Call<Customer> call = apiService.createCustomer(customer);

        call.enqueue(new Callback<Customer>() {

            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                Log.d("RESPONSE", response.code() + " ");
                if (response.code() == 200) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
