package iut.paci.paciapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    TextView msgNameView , msgPasswordView, result;
    private static final String TAG ="Resultat Connexion :";
    Toast t;
    ProgressDialog pDialog;
    String urlString = "http://localhost/exo_android/ident.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth mauth = FirebaseAuth.getInstance();
        Intent intent = new Intent(this,AccueilActivity.class);
        Bundle bundle = new Bundle();
        result = (TextView) findViewById(R.id.resultView);

        Button b = (Button) findViewById(R.id.Authentifier);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgNameView = (TextView) findViewById(R.id.PersonName);
                msgPasswordView = (TextView) findViewById(R.id.PersonPassword);
                String login = msgNameView.getText().toString();
                String mot_de_passe = msgPasswordView.getText().toString();
                if(TextUtils.isEmpty(login)){
                    result.setText("Login vide");
                    result.setTextColor(Color.RED);
                    Toast t = Toast.makeText(view.getContext(), "Champs vides", Toast.LENGTH_SHORT);
                    t.show();
                    Log.i(TAG,"Un ou les deux champs sont vides");
                }else if(TextUtils.isEmpty(mot_de_passe)){
                    result.setText("Mot de passe vide");
                    result.setTextColor(Color.RED);
                    Toast t = Toast.makeText(view.getContext(), "Champs vides", Toast.LENGTH_SHORT);
                    t.show();
                    Log.i(TAG,"Un ou les deux champs sont vides");
                }else{
                    mauth.signInWithEmailAndPassword(login,mot_de_passe).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()){
                                   Log.i("Login: ",login);
                                   Log.i("Mot De Passe: ",mot_de_passe);
                                   Log.v("Connexion Internet: ", Boolean.toString(checkConnection(view.getContext())));
                                   result.setText("Valide !!!");
                                   result.setTextColor(Color.GREEN);
                                   t = Toast.makeText(view.getContext(),"Connexion réussie",Toast.LENGTH_SHORT);
                                   t.show();
                                   Log.i(TAG,"Connexion réussie");

                                   bundle.putString("login",login);
                                   bundle.putString("password",mot_de_passe);
                                   intent.putExtras(bundle);
                                   startActivity(intent);
                               }else{
                                   result.setText("Champs erronés");
                                   result.setTextColor(Color.RED);
                                   Toast t = Toast.makeText(view.getContext(), "Login ou Mot de passe erroné", Toast.LENGTH_SHORT);
                                   t.show();
                                   Log.i(TAG,"Erreur lors de la saisie");
                               }
                           }
                       }
                    );


                }



            }
        });


    }


    public void getConnection() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Getting list of friends...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Ion.with(this)
                .load(urlString)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pDialog.dismiss();
                        if(result == null)
                            Log.d(TAG, "No response from the server!!!");
                        else {
                            if(result.equals("true")){
                                Log.d(TAG,"connexion etablie");
                            }else{
                                Log.i(TAG,"connexion non établie");
                            }
                            // Traitement de result

                        }
                    }
                });
    }


    public static boolean checkConnection(Context ctx) {
        ConnectivityManager connectivityManager =

                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI &&
                        networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            // No internet connection
            return false;
        } else
            return true;

    }
}