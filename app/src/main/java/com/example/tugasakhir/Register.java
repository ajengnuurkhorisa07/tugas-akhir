package com.example.tugasakhir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputEditText nik, nama, email, telepon, alamat;
    Button register;
    TextView login;
    ImageView back;
    Dialog mDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nik = (TextInputEditText) findViewById(R.id.nik_regis);
        nama = (TextInputEditText) findViewById(R.id.nama_regis);
        email = (TextInputEditText) findViewById(R.id.email_regis);
        telepon = (TextInputEditText) findViewById(R.id.hp_regis);
        alamat = (TextInputEditText) findViewById(R.id.alamat_regis);
        mDialog = new Dialog(this);

        register = (Button) findViewById(R.id.btn_register);
        login = (TextView) findViewById(R.id.txt_login);
        progressDialog = new ProgressDialog(Register.this);
        back = (ImageView) findViewById(R.id.img_back);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sNik = nik.getText().toString();
                String sNama = nama.getText().toString();
                String sEmail = email.getText().toString();
                String sTelepon = telepon.getText().toString();
                String sAlamat = alamat.getText().toString();

                if (!sNik.isEmpty()) {
                    CreateDataToServer(sNik, sNama, sEmail, sTelepon, sAlamat);
                    final Dialog dialog = new Dialog(Register.this);
                    dialog.setContentView(R.layout.popup_register);

                    TextView text = (TextView) dialog.findViewById(R.id.txt_berhasil);
                    text.setText("REGISTRASI BERHASIL");
                    ImageView image = (ImageView) dialog.findViewById(R.id.img_info);
                    image.setImageResource(R.drawable.ic_baseline_info_24);

                    Button dialogButton = (Button) dialog.findViewById(R.id.okbtn);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent loginIntent = new Intent(Register.this, Login.class);
                            startActivity(loginIntent);
                        }
                    });

                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                }


            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void CreateDataToServer(String nik, String nama, String email, String telepon, String alamat) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.SERVER_REGISTER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String resp = jsonObject.getString("server_response");
                        if (resp.equals("[{\"status\":\"OK\"}]")) {
                        } else {
                            Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("nik", nik);
                    params.put("nama", nama);
                    params.put("email", email);
                    params.put("telepon", telepon);
                    params.put("alamat", alamat);
                    return params;

                }
            };

            Connection.getInstance(Register.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}

