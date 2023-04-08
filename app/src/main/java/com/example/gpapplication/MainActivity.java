package com.example.gpapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gpapplication.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.*;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.security.Permission;
import java.security.Permissions;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity //implements OnSuccessListener
         {
    // Google Fit API variables
    int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    // private final String TAG = "MainActivity";
    private FitnessOptions fitnessOptions;
    private FitnessDataResponseModel fitnessDataResponseModel;  // commented because of the eror it shows
    private ActivityMainBinding activityMainBinding;

    //static int n=2;
    //FirebaseDatabase rootNode;
    // Signing in using email and password variables
    DatabaseReference reference;
    EditText emailaddress, Password;
    FirebaseAuth mAuth;
    ImageView Google;
    // Signin using google api variables
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    //FitnessOptions fitnessOptions;
    Button Signup_button, Signin_button, charts_button;



    // @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial_definitions();
        define_buttons_actions();
        //initialization();
        //subscribeAndGetRealTimeData(DataType.TYPE_STEP_COUNT_DELTA);

    }
    private void initial_definitions(){
       // activityMainBinding = DataBinding.setContentView(this, R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        emailaddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        Password = findViewById(R.id.editTextTextPassword);
        Google = findViewById(R.id.Google);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        Signup_button = (Button) findViewById(R.id.Sign_Up_button);
        Signin_button = (Button) findViewById(R.id.signin_button);
        charts_button= (Button) findViewById(R.id.charts);
    }
    private void define_buttons_actions(){
        Signin_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login();
            }
        });
        Signup_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Register();
            }
        });
        Google.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Logingoogle();
            }
        });
        charts_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToCharts();
            }
        });
    }
    private void goToCharts(){
        Intent intent=new Intent(MainActivity.this, Charts.class);
        startActivity(intent);
    }
    // signing in using google api funbctions
    public void Logingoogle() { // Logon using google  api
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                HomeActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void HomeActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), NewActivty.class);
        startActivity(intent);
    }

    public void Login() { // Loging in using email and password function
        String email = emailaddress.getEditableText().toString(); // fetching email entered by the user
        String password = Password.getEditableText().toString(); // fetching password entered by the user
        if (TextUtils.isEmpty(email)) {  // to display a message if the user did not enter a username
            Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {  // to display a message if the user did not enter a password
            Toast.makeText(MainActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {  // Signing in happend successfully
                            Toast.makeText(MainActivity.this, "Login Successfully.",
                                    Toast.LENGTH_SHORT).show(); // Displaying a meassage to the user indicating a successful login
                            startActivity(new Intent(MainActivity.this, Charts.class));
                        } else { // There is an error in the signing in process
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show(); // Displaying an error message to the user
                        }
                    }
                });
    }

    public void Register() { // Regitering a new user in the firebase using email and password
        String email = String.valueOf(emailaddress.getText()); // fetching email entered by the user
        String password = String.valueOf(Password.getText()); // fetching password entered by the user
        if (TextUtils.isEmpty(email)) { // to display a message if the user did not enter a username
            Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) { // to display a message if the user did not enter a password
            Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        //FirebaseAuth auth;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // Signing up happend successfully
                    Toast.makeText(MainActivity.this, "Registartion Successful", Toast.LENGTH_LONG).show();
                    // Displaying a meassage to the user indicating a successful signup
                    FirebaseUser user = mAuth.getCurrentUser();

                } else {  // There is an error in the signing up process
                    Toast.makeText(MainActivity.this, "Registartion failed", Toast.LENGTH_LONG).show();
                    // Displaying an error message tot he user
                }
            }
        });
    }


// Google fit api functions


    private void initialization() {
        fitnessDataResponseModel = new FitnessDataResponseModel();
        // activityMainBinding
    }

    private void getDataUsingSensor(DataType dataType) {

        Fitness.getSensorsClient(this, getGoogleAccount())
                .add(new SensorRequest.Builder()
                                .setDataType(dataType)
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        new OnDataPointListener() {
                            @Override
                            public void onDataPoint(@NonNull DataPoint dataPoint) {
                                float value = Float.parseFloat(dataPoint.getValue(Field.FIELD_STEPS).toString());
                                fitnessDataResponseModel.steps = Float.parseFloat(new DecimalFormat("#.##").format(value));
                                 activityMainBinding.setFitnessData(fitnessDataResponseModel);
                            }
                        });

    }

    private void checkGoogleFitPermission() {
        fitnessOptions = fitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        GoogleSignInAccount account = getGoogleAccount();
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(MainActivity.this, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, account, fitnessOptions);
        } else {
            startDataRendering();
        }
    }

    private void startDataRendering() {
        getTodayData();
        subscribeAndGetRealTimeData(DataType.TYPE_STEP_COUNT_DELTA);
    }

    private void getTodayData() {
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(this);
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(this);
        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(this);
    }

    private void subscribeAndGetRealTimeData(DataType datatype) {
        Fitness.getRecordingClient(this, getGoogleAccount())
                .subscribe(datatype)
                .addOnSuccessListener(avoid -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
                });

        getDataUsingSensor(datatype);
    }

    private void requestForHistory() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();

        cal.set(2023, 3, 28);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0); //so it get all day and not the current minute
        cal.set(Calendar.SECOND, 0);
        long startTime = cal.getTimeInMillis();


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_CALORIES_EXPENDED)
                .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(DataType.TYPE_DISTANCE_DELTA)
                .aggregate(DataType.AGGREGATE_DISTANCE_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        Fitness.getHistoryClient(this, getGoogleAccount())
                .readData(readRequest)
                .addOnSuccessListener(this);

    }

    public void checkPermissions() {LOCATION_PERMISSION)
        if (!PermissionManager.hasPermissions(this, Permissions.) {

        }
    }

    GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(MainActivity.this, fitnessOptions);
    }

    @Override
    public void onSuccess(Object o) {
        if (o instanceof DataSet) {
            DataSet dataset = (DataSet) o;
            if (dataset != null) {
                getDataFromDataSet(dataset);
            }
        } else if (o instanceof DataReadResponse) {
            fitnessDataResponseModel.steps = 0f;
            fitnessDataResponseModel.calories = 0f;
            fitnessDataResponseModel.distance = 0f;
            DataReadResponse dataReadResponse = (DataReadResponse) o;
            if (dataReadResponse.getBuckets() != null && !dataReadResponse.getBuckets().isEmpty()) {
                List<Bucket> bucketList = dataReadResponse.getBuckets();
                if (bucketList != null && !bucketList.isEmpty()) {
                    for (Bucket bucket : bucketList) {
                        DataSet stepsDataSet = bucket.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
                        getDataFromDataReadResponse(stepsDataSet);
                        DataSet caloriesDataSet = bucket.getDataSet(DataType.TYPE_CALORIES_EXPENDED);
                        getDataFromDataReadResponse(caloriesDataSet);
                        DataSet distanceDataSet = bucket.getDataSet(DataType.TYPE_DISTANCE_DELTA);
                        getDataFromDataReadResponse(distanceDataSet);
                    }
                }
            }
        }
    }


    private void getDataFromDataSet(DataSet dataset) {
        List<DataPoint> dataPoints= dataset.getDataPoints();
        for(DataPoint datapoint: dataPoints){

            for (Field field : datapoint.getDataType().getFields()){
                float value= Float.parseFloat(datapoint.getValue(field).toString());

                if(field.getName().equals(Field.FIELD_STEPS.getName())){
                    fitnessDataResponseModel.steps= Float.parseFloat(new DecimalFormat("#.##").format(value));
                }
                else if(field.getName().equals(Field.FIELD_CALORIES.getName())){
                    fitnessDataResponseModel.calories= Float.parseFloat(new DecimalFormat("#.##").format(value));
                }
                else if(field.getName().equals(Field.FIELD_DISTANCE.getName())){
                    fitnessDataResponseModel.distance= Float.parseFloat(new DecimalFormat("#.##").format(value));
                }

            }
        }
       // activityMainBinding.setFitnessData(fitnessDataResponseModel);
    }

    private void getDataFromDataReadResponse(DataSet dataset){

    }


}
 /*

  */
    /*rootNode=FirebaseDatabase.getInstance();
        reference= rootNode.getReference();
        String email=emailaddress.getEditableText().toString();
        String password=Password.getEditableText().toString();
        User user=new User(email,password);
        Toast.makeText(MainActivity.this,"object is created",Toast.LENGTH_LONG).show();
        reference.child(String.valueOf(n)).setValue(user);
        n++;
        startActivity(new Intent(MainActivity.this, NewActivty.class));
         */




// login & signup using google api
// login and signup using facebook api // not implemented
// login & signup using firebase (email & password) doone

// charts(barchart and piechart) --> Dynamic
// google fit api --> errors

// permissions (started debugging)
// and activitymainbinding (needs more investigation)

// other Apis and Integration--> standard format as input & output
// facebook api implementation
// googlefitapi
// Loging in using google api