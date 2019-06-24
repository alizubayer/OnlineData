package com.example.onlinedata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText name,username,email,password,cpassword;
    private Button singup;

    String Name,UserName,Email,Password,CPassword;

    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.name);
        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        cpassword = (EditText)findViewById(R.id.cpassword);

        singup = (Button)findViewById(R.id.singup);

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString();
                UserName = username.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();
                CPassword = cpassword.getText().toString();

                if(Name.isEmpty())
                {
                    name.setError("Enter Name Please");
                    name.requestFocus();

                }
                else if (UserName.isEmpty())
                {
                    username.setError("Enter UserName Please");
                    username.requestFocus();
                }
               else if (Email.isEmpty())
                {
                    email.setError("Enter Email Please");
                    email.requestFocus();
                }
               else if (Password.isEmpty())
                {
                    password.setError("Enter Password Please");
                    password.requestFocus();
                }
                else if (CPassword.isEmpty())
                {
                    cpassword.setError("Enter Confirm Password Please");
                    cpassword.requestFocus();
                }

                else if (!(Password.equalsIgnoreCase(CPassword)))
                {
                    cpassword.setError("Password is not matched");
                    cpassword.requestFocus();
                }
                else

                {
                    loading = new ProgressDialog(MainActivity.this);
                    loading.setIcon(R.drawable.load);
                    loading.setTitle("Login");
                    loading.setMessage("Please wait...");
                    loading.show();

                    HttpsTrustManager.allowAllSSL();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.SIGNUP_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //for track response in Logcat
                            Log.d("RESPONSE", "" + response);

                            //if we are getting success from server
                            if (response.equals("success")) {
                                //creating a shared preference
                                loading.dismiss();
                                //starting profile activity
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "Sign Up Successfull", Toast.LENGTH_SHORT).show();

                                name.setText("");
                                username.setText("");
                                email.setText("");
                                password.setText("");
                                cpassword.setText("");


                            } else if (response.equals("exists")) {
                                Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            } else if (response.equals("failure")) {
                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    Log.d("Error", "" + error);
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //return super.getParams();

                            Map<String,String> params = new HashMap<>();

                            //Ading parameters to request
                            params.put(Key.KEY_NAME,Name);
                            params.put(Key.KEY_USER,UserName);
                            params.put(Key.KEY_EMAIL,Email);
                            params.put(Key.KEY_PASSWORD,Password);
                            params.put(Key.KEY_CPASSWORD,CPassword);


                            //returning parameter
                            return params;

                        }
                    };

                    //Adding the string request to the queue
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);

                }


            }
        });

    }
}
