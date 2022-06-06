package com.example.firestore_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private  final  String KEY_TITLE="title";
    private  final  String KEY_DESCRIPTION="description";

//    private ListenerRegistration noteListener;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

//    private DocumentReference noteRef=db.collection("noteBook").document("my first note");
      private DocumentReference noteRef=db.document("NoteBook/my first note"); // to read a precise document in a collection // my first note is the key of the document

//        CollectionReference noteBook=db.collection("NoteBook"); // to refere a collection

    EditText editText_title;
    EditText editText_description;

    TextView textView_data_retrieved;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialisations ###################################################
        editText_title=findViewById(R.id.Edit_text_title);
        editText_description=findViewById(R.id.Edit_text_description);

        textView_data_retrieved=findViewById(R.id.data_retrieved);
        //##################################################################################""

    }

    @Override
    protected void onStart() {
        super.onStart();
//     noteListener=
             noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if(error!=null){
                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,error.toString());
                    return;
                }

                if(documentSnapshot.exists()){

                    Note note=documentSnapshot.toObject(Note.class);

                    String title=note.getTitle();
                    String description=note.getDescription();

                    textView_data_retrieved.setText("Title: "+title+"\n"+"Description:"+description);

                }
                else {
                    textView_data_retrieved.setText("");
                }

            }
        });
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        noteListener.remove();
//    }


    ////// ########################## saveNote methode

    public  void saveNote(View v){

        String title=editText_title.getText().toString();
        String description=editText_description.getText().toString();

//        Map<String,Object> note=new HashMap<>();
//        note.put(KEY_TITLE,title);
//        note.put(KEY_DESCRIPTION,description);

        Note note=new Note(title,description,1);


      noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public  void onSuccess(Void unused) {

                        Toast.makeText(MainActivity.this, " Note saved Successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, " Error!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());

                    }
                })
        ;
    }


    public  void updateDescription(View v){
        String description= editText_description.getText().toString();

        Map<String,Object> note=new HashMap<>();
        note.put(KEY_DESCRIPTION,description);
//       / noteRef.set(note, SetOptions.merge()); // we add merge if we want to change only the description if the document does not exist, it will create a new one with only the description field

//        noteRef.update(note);
        noteRef.update(KEY_DESCRIPTION,description); // update just update de current field, if it does not exist, no creation

    }

    public  void deleteDescription(View v){
        Map<String,Object> note=new HashMap<>();

        note.put(KEY_DESCRIPTION, FieldValue.delete());
//        noteRef.update(note);
        noteRef.update(KEY_DESCRIPTION,FieldValue.delete()); // we can add this
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

    }

    public  void DeleteNote(View v){

        noteRef.delete();

        // we can add this
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

    }


    public void retrieveNote(View v){ // single note to read
//        private DocumentReference noteRef=db.document("NoteBook/my first note"); to declare outside
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
//
//                            String title= documentSnapshot.getString(KEY_TITLE);
//                            String description=documentSnapshot.getString(KEY_DESCRIPTION);
//                            // or
////                           Map<String,Object> note=documentSnapshot.getData();

                            Note note=documentSnapshot.toObject(Note.class);

                              String title=note.getTitle();
                              String description=note.getDescription();

                            textView_data_retrieved.setText("Title: "+title+"\n"+"Description:"+description);

                        }
                        else {
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, " Error!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                })

        ;

    }


    public void gotosecond(View v){
        startActivity(new Intent(MainActivity.this,Second_activity.class));
    }

}