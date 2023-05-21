package com.example.tugasakhir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class HalamanEditAkun extends AppCompatActivity {
    TextInputEditText Nik, Nama, Email, Telepon, Alamat;
    Button simpan, batal;
    ProgressDialog progressDialog;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_edit_akun);

        Nik = findViewById(R.id.elNik);
        Nama = findViewById(R.id.elNama);
        Email = findViewById(R.id.elEmail);
        Telepon = findViewById(R.id.elNomor);
        Alamat = findViewById(R.id.elAlamat);
        batal = findViewById(R.id.btnBatal);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        simpan = findViewById(R.id.btnSimpan);
        batal = findViewById(R.id.btnBatal);

        Intent i = getIntent();
        String mNik = i.getStringExtra("nik");
        String mNama = i.getStringExtra("nama");
        String mEmail = i.getStringExtra("email");
        String mTelepon = i.getStringExtra("telepon");
        String mAlamat = i.getStringExtra("alamat");

        Nik.setText(mNik);
        Nama.setText(mNama);
        Email.setText(mEmail);
        Telepon.setText(mTelepon);
        Alamat.setText(mAlamat);

        batal = (Button) findViewById(R.id.btnBatal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanEditAkun.this, AkunFragment.class);
                startActivity(intent);
            }
        });
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanEditAkun.this, AkunFragment.class);
                startActivity(intent);
            }
        });
        simpan = (Button) findViewById(R.id.btnSimpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nik = Nik.getText().toString().trim();
                String nama = Nama.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String telepon = Telepon.getText().toString().trim();
                String alamat = Alamat.getText().toString().trim();
                if (nik.isEmpty() || nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || alamat.isEmpty()) {
                    massage("some feilds are empty!");
                } else {
                    progressDialog.setTitle("Updating....");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.UPDATE_USER_INFO_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    massage(response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            massage(error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> updateParams = new HashMap<>();
                            updateParams.put("nik", nik);
                            updateParams.put("nama", nama);
                            updateParams.put("email", email);
                            updateParams.put("telepon", telepon);
                            updateParams.put("alamat", alamat);
                            updateParams.put("mynik", mNik);
                            return updateParams;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(HalamanEditAkun.this);
                    queue.add(stringRequest);
                }

            }
        });
    }

    public void massage(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }

}