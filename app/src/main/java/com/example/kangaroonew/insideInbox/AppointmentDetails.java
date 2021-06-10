package com.example.kangaroonew.insideInbox;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kangaroonew.Inbox;
import com.example.kangaroonew.JsonApiPlaceholder;
import com.example.kangaroonew.R;
import com.example.kangaroonew.models.AppointmentClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
public class AppointmentDetails extends AppCompatActivity {
    int userID;
    JsonApiPlaceholder jsonPlaceHolder;
    String[] mobileArray;
//    = {"Android","IPhone","WindowsMobile","Blackberry",
//            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        userID=getIntent().getExtras().getInt("userID");

        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);

        checkingAppointments(this);



//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.list_view, mobileArray);
//
//        ListView listView = (ListView) findViewById(R.id.mobile_list);
//        listView.setAdapter(adapter);
    }



    private void checkingAppointments(final Context context) {


        final Call<List<AppointmentClass>> appointmentList=jsonPlaceHolder.userAppointments(userID);
        appointmentList.enqueue(new Callback<List<AppointmentClass>>() {

            @Override
            public void onResponse(Call<List<AppointmentClass>> call, Response<List<AppointmentClass>> response) {

                if(response.isSuccessful()){

                    List<AppointmentClass> appointmentList=response.body();

                   mobileArray=new String[appointmentList.size()];
                   String temp="";
                    for(AppointmentClass appointment: appointmentList){
                        temp="Appointment at ";
                        temp+=appointment.getHospitalId().toString();  //send to API to get names values
                        temp+=" with "+appointment.getStaffId();
                        temp+="\n";
                        temp+="Description: "+"\n";
                        temp+=appointment.getDescription();
                        temp+="\n";
                        temp+="Date: "+appointment.getDate();
                        temp+="  Time: "+appointment.getAppointment_time();

                        //fill this appointment in array
                        mobileArray[appointmentList.indexOf(appointment)]=temp;

                        //set temp to empty for next loop
                        temp="";
                        Log.d("Ds","status is "+appointment.getStatus());
//                        if(TextUtils.equals(appointment.getStatus(),"1")){//the appointment is accepted
//                            Log.d("sdf","response is "+appointment.getDate());
//                            String txt="Your appointment is on ";
//                            appointmentText.setText(txt+appointment.getDate());
//                            found=true;
//                        }

                    }


                    ArrayAdapter adapter = new ArrayAdapter<String>(context,
                            R.layout.list_view, mobileArray);

                    ListView listView = (ListView) findViewById(R.id.mobile_list);
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<AppointmentClass>> call, Throwable t) {

                Toast.makeText(AppointmentDetails.this,"Failed to load, error occured!",Toast.LENGTH_LONG).show();
                Log.d("Ds","status is NOT HERE");
            }
        });

    }
}