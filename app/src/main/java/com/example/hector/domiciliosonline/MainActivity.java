package com.example.hector.domiciliosonline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.hector.domiciliosonline.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    RelativeLayout rootLayout;

    //Firebase
    FirebaseAuth  auth;
    FirebaseDatabase db;
    DatabaseReference users;



    //Atajo Ctr + O
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase  ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Contenido de la vista
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                            .setDefaultFontPath("fonts/Arkhip_font.ttf")
                                            .setFontAttrId(R.attr.fontPath)
                                            .build());
        setContentView(R.layout.activity_main);

        //Inicializando Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");



        //Inicializando Vistas
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        //Evento Botones

         btnRegister.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mostrarDialogoRegistro2();
                 //mostrarDialogoRegistro();

             }
         });

         btnSignIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mostrarDialogoLogin();
             }
         });

    }

    private void mostrarDialogoRegistro2() {
        final AlertDialog.Builder dialogo =  new AlertDialog.Builder(this);
        dialogo.setTitle("Registrarse");
        dialogo.setMessage("Porfavor usar email para registrarse");

        LayoutInflater inf = LayoutInflater.from(this);
        //Inicializando vista Layout Registro
        View register_layout = inf.inflate(R.layout.layout_register,null);

        //Buscando edt.Layout
        final MaterialEditText edt_Email = register_layout.findViewById(R.id.edt_Email);
        final MaterialEditText edt_Password = register_layout.findViewById(R.id.edt_Password);
        final MaterialEditText edt_Nombre = register_layout.findViewById(R.id.edt_Name);
        final MaterialEditText edt_Celular = register_layout.findViewById(R.id.edt_Phone);

        dialogo.setView(register_layout);

        //Colocar Boton
        dialogo.setPositiveButton("REGISTRARSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //Validaciones.
                if (TextUtils.isEmpty(edt_Email.getText().toString()))
                {
                    Snackbar.make(rootLayout,"Por favor ingresar email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_Celular.getText().toString()))
                {
                    Snackbar.make(rootLayout,"Por favor ingresar telefono ",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_Password.getText().toString()))
                {
                    Snackbar.make(rootLayout,"Por favor ingresar contraseña",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (edt_Password.getText().toString().length() < 6)
                {
                    Snackbar.make(rootLayout,"Contraseña muy corta : Digitar mas de 6 caracteres.",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Registar Usuario

                auth.createUserWithEmailAndPassword(edt_Email.getText().toString(),edt_Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                             // Guardar usuario
                                User user = new User();
                                user.setEmail(edt_Email.getText().toString());
                                user.setName(edt_Nombre.getText().toString());
                                user.setPhone(edt_Celular.getText().toString());
                                user.setPassword(edt_Password.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout,"Registrado !.",Snackbar.LENGTH_SHORT).show();
                                            }
                                        })

                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout,"Error" + e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout,"Error " + e.getMessage(),Snackbar.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        dialogo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogo.show();

    }

    private void mostrarDialogoLogin() {
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("SIGN IN");
        dialogo.setMessage("Porfavor usar email para ingresar");

        LayoutInflater inf = LayoutInflater.from(this);
        //Inicializando vista Layout Registro
        View login_layout = inf.inflate(R.layout.layout_login, null);

        //Buscando edt.Layout
        final MaterialEditText edt_Email = login_layout.findViewById(R.id.edt_Email);
        final MaterialEditText edt_Password = login_layout.findViewById(R.id.edt_Password);

        dialogo.setView(login_layout);



        //Colocar Boton
        dialogo.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                //Bloquear boton Sign in
                btnSignIn.setEnabled(false);

                //Validacion.
                if (TextUtils.isEmpty(edt_Email.getText().toString())) {
                    Snackbar.make(rootLayout, "Por favor ingresar email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_Password.getText().toString())) {
                    Snackbar.make(rootLayout, "Por favor ingresar contraseña", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (edt_Password.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Contraseña muy corta : Digitar mas de 6 caracteres.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                 final android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();
                dialogInterface.dismiss();

                //Logearse
                auth.signInWithEmailAndPassword(edt_Email.getText().toString(), edt_Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, Welcome.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout, "Error al loguearse" + e.getMessage(), Snackbar.LENGTH_SHORT).show();


                        //Activar Boton
                        btnSignIn.setEnabled(true);
                    }
                });


            }
        });

        dialogo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        dialogo.show();
    }


}
