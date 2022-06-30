package iut.paci.paciapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccueilActivity extends AppCompatActivity {
    FirebaseFirestore  db;
    RecyclerView mRecyclerView;
    FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        mRecyclerView=findViewById(R.id.RecyclerAmis);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        db=FirebaseFirestore.getInstance();
        CollectionReference collRef= db.collection("Utilisateurs");
        Query query=collRef.orderBy("Nom").limit(3);
        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot listDoc = task.getResult();
                    for (QueryDocumentSnapshot doc : listDoc) {
                        Log.i("adev","Document actuel:"+doc.getData());
                    }
                }
            }
        });

        FirestoreRecyclerOptions<Utilisateurs> options=new FirestoreRecyclerOptions.Builder<Utilisateurs>().setQuery(query,
                Utilisateurs.class).build();
        adapter = new FirestoreRecyclerAdapter<Utilisateurs,UtilisateursVH>(options){

            @NonNull
            @Override
            public UtilisateursVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amis,parent,false)  ;
                return new UtilisateursVH(layout);
            }

            @Override
            protected void onBindViewHolder(@NonNull UtilisateursVH holder, int position, @NonNull Utilisateurs model) {
                holder.mNom.setText(model.getmNom());
                holder.mPrenom.setText(model.getmPrenom());
                holder.mScore.setText(""+model.getmScore());

            }
        };
    mRecyclerView.setAdapter(adapter);

    }
        @Override
            protected void onStart(){
                super.onStart();
                adapter.startListening();
            }

    @Override
            protected void onStop() {
                super.onStop();
                adapter.startListening();
            }
    class UtilisateursVH extends RecyclerView.ViewHolder {
        TextView mNom;
        TextView mPrenom;
        TextView mScore;

        public UtilisateursVH(@NonNull View itemView) {
            super(itemView);
            mNom = itemView.findViewById(R.id.Nom);

            mPrenom = itemView.findViewById(R.id.Prenom);
            mScore = itemView.findViewById(R.id.Score);
        }
    }

}
