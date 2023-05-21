package com.example.tugasakhir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputEditText Nik, Password;
    TextView textView;
    ImageView imageView;
    Button login;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Nik = (TextInputEditText) findViewById(R.id.el2);
        Password = (TextInputEditText) findViewById(R.id.el_3);
        login = (Button) findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        textView = (TextView) findViewById(R.id.txt_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        imageView = (ImageView) findViewById(R.id.img_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, TampilanAwal.class);
                startActivity(intent);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLoginProcces();
            }
        });

        textView = (TextView) findViewById(R.id.txt_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, AkunFragment.class);
                startActivity(intent);
            }
        });

        imageView = (ImageView) findViewById(R.id.img_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, TampilanAwal.class);
                startActivity(intent);
            }
        });

    }

    private void UserLoginProcces() {
        String nik = Nik.getText().toString().trim();
        String password = Password.getText().toString().trim();
        if (nik.isEmpty() || password.isEmpty()) {
            massage("Terdapat Field yang Kosong");
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Url.SERVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("status");
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (result.equals("success")) {
                                    progressDialog.dismiss();
                                    for (int i= 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String nik = object.getString("nik");
                                        String nama = object.getString("nama");
                                        String email = object.getString("email");
                                        String telepon = object.getString("telepon");
                                        String alamat = object.getString("alamat");
                                        String password = object.getString("password");


                                        Intent intent = new Intent(Login.this, HalamanEditAkun.class);
                                        intent.putExtra("nik",nik);
                                        intent.putExtra("nama",nama);
                                        intent.putExtra("email",email);
                                        intent.putExtra("telepon",telepon);
                                        intent.putExtra("alamat",alamat);
                                        intent.putExtra("password",password);
                                        startActivity(intent);finish();

                                        massage("Login Berhasil");

                                    }

                                } else {
                                    progressDialog.dismiss();
                                    massage("Login Gagal!!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    massage(error.getMessage());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nik", nik);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(Login.this);
            queue.add(request);
        }
    }

    public void massage(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();

    }
}