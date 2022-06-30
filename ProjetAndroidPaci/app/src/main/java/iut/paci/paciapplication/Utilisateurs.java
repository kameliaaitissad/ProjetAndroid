package iut.paci.paciapplication;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Utilisateurs {
    private String mNom;
    private String mPrenom;
    private int mScore;
    private Date mTimestamp;

    public Utilisateurs() { } // Needed for Firebase

    public Utilisateurs(String Nom, String Prénom, int Score) {
        mNom = Nom;
        mPrenom = Prénom;
        mScore = Score;
    }

    public String getmNom() { return mNom; }

    public void setmNom(String name) { mNom = name; }

    public String getmPrenom() { return mPrenom; }

    public void setmPrenom(String prenom) { mPrenom = prenom; }

    public int getmScore() { return mScore; }

    public void setmScore(int score) { mScore = score; }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}
