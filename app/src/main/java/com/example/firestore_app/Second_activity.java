package com.example.firestore_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Second_activity extends AppCompatActivity {
    private static final String TAG = "Second_activity";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteBookRef = db.collection("NoteBook");


    EditText editText_title;
    EditText editText_description;
    EditText editText_priority;

    TextView textView_data_retrieved_second;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        //initialisations ###################################################
        editText_title = findViewById(R.id.Edit_text_title_second);
        editText_description = findViewById(R.id.Edit_text_description_second);
        editText_priority = findViewById(R.id.Edit_text_priority_second);

        textView_data_retrieved_second = findViewById(R.id.data_retrieved_second);


    }


    @Override
    protected void onStart() {
        super.onStart();

        noteBookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    return;
                }
                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();
                    int priority = note.getPriority();

                    data += "Title: " + title + "\n" + "Description: " + description + "\n" + "DoucmentId :" + documentId + "\n" + "Priority: " + priority + "\n";


                }

                textView_data_retrieved_second.setText(data);


            }
        });

    }








    public void AddNote(View v) {

        String title = editText_title.getText().toString();
        String description = editText_description.getText().toString();

        if (editText_priority.length() == 0) {
            editText_priority.setText("0");
        }

        int priority = Integer.parseInt(editText_priority.getText().toString());

        Note note = new Note(title, description, priority,null,null);


        //noteBookRef.document("customkey").set(note);

        noteBookRef.add(note);

        //onsuccsess.......................

        ;
    }

    public void LoadNote(View v) {

        //to sort our data efficiently

        Task task1 = noteBookRef.
                whereLessThan("priority", 2)
                .orderBy("priority")
                .orderBy("title")
                .get();
                // OR operator
        Task task2 = noteBookRef.
                whereGreaterThan("priority", 2)
                .orderBy("priority")
                .get();

        Task<List<QuerySnapshot>> allTasls = Tasks.whenAllSuccess(task1, task2);

        allTasls.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {

                String data = "";

                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());

                        String documentId = note.getDocumentId();
                        String title = note.getTitle();
                        String description = note.getDescription();
                        int priority = note.getPriority();


                        data += "Title: " + title + "\n" + "Description: " + description + "\n" + "DoucmentId :" + documentId + "\n" + "Priority: " + priority + "\n";

                    }
                }

                textView_data_retrieved_second.setText(data);

            }
        });

        //####################################################################################
        //OR
//        noteBookRef.
//                whereGreaterThanOrEqualTo("priority",2)
//                .orderBy("priority")
//                .orderBy("title")
    ////                .whereEqualTo("title","Aa")// we can combine multiple query
//
//                .limit(3)
////                .whereEqualTo("priority",2)// Optional just data with priority 2
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) { // multiple snapshot with querydocumentsnapshot
//
//                        String data="";
//
//                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots ){
//                            Note note =documentSnapshot.toObject(Note.class);
//                            note.setDocumentId(documentSnapshot.getId());
//
//                            String documentId=note.getDocumentId();
//                            String title= note.getTitle();
//                            String description=note.getDescription();
//                            int priority=note.getPriority();
//
//
//                            data+="Title: "+title+"\n"+"Description: "+description+"\n"+"DoucmentId :"+documentId+"\n"+"Priority: "+priority+"\n";
//
//                        }
//
//                        textView_data_retrieved_second.setText(data);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG,e.toString());
//                    }
//                });

    }

    public void GotoPagination(View v) {
        Intent intent = new Intent(this, Pagination_and_others.class);
        startActivity(intent);
    }

    }
