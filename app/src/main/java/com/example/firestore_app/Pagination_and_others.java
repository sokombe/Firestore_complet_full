package com.example.firestore_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pagination_and_others extends AppCompatActivity {

    EditText editText_title;
    EditText editText_description;
    EditText editText_priority;
    EditText editText_tags;
    EditText getEditText_tags_map;

    TextView textView_data_retrieved_second;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteBookRef = db.collection("NoteBook");


    DocumentSnapshot lastresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination);


        //initialisations ###################################################
        editText_title = findViewById(R.id.Edit_text_title_pagination);
        editText_description = findViewById(R.id.Edit_text_description_pagination);
        editText_priority = findViewById(R.id.Edit_text_priority_pagination);
        editText_tags = findViewById(R.id.Edit_text_description_pagination_tags);
        getEditText_tags_map=findViewById(R.id.Edit_text_description_pagination_tags_map);

        textView_data_retrieved_second = findViewById(R.id.data_retrieved_pagination);



//        executeTransaction();

//        executeBatchWrite();

        updateArray();

    }



//    @Override
//    protected void onStart() {
//
//        // to add a new document listener to the collection reference  ###################################################
//        super.onStart();
//        noteBookRef.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    return;
//                }
//                if (value != null) {
//                    for (DocumentChange doc : value.getDocumentChanges()) {
//
//                    DocumentSnapshot docSnapshot = doc.getDocument();
//                    String id = docSnapshot.getId();
//
//                    int oldIndex = doc.getOldIndex();
//                    int newIndex = doc.getNewIndex();
//
//                    switch (doc.getType()) {
//                        case ADDED:
//                            textView_data_retrieved_second.setText(textView_data_retrieved_second.getText() + "\n" + "Added: " + id + " at " + newIndex+ "oldIndex: "+oldIndex+"\n\n");
//                            break;
//                        case MODIFIED:
//                            textView_data_retrieved_second.setText(textView_data_retrieved_second.getText() + "\n" + "Modified: " + id + " at " + newIndex+ "oldIndex: "+oldIndex+"\n\n");
//                            break;
//                        case REMOVED:
//                            textView_data_retrieved_second.setText(textView_data_retrieved_second.getText() + "\n" + "Removed: " + id + " at " + oldIndex+ "oldIndex: "+oldIndex+"\n\n");
//                            break;
//                    }
//
//                    }
//                }
//            }
//        });
//    }




//####################################################################



    public void AddNote(View v) {

        String title = editText_title.getText().toString();
        String description = editText_description.getText().toString();

        if (editText_priority.length() == 0) {
            editText_priority.setText("0");
        }

        int priority = Integer.parseInt(editText_priority.getText().toString());
        // for  array
        String tags = editText_tags.getText().toString();
        String[] tags_array = tags.split("\\s*,\\s*");

        List<String> tags_list = java.util.Arrays.asList(tags_array);

        //        for map
        String tags_map = getEditText_tags_map.getText().toString();
        String[] tags_array_map = tags_map.split("\\s*,\\s*");
        Map<String, Boolean> tags_map_map = new HashMap<>();

        for (String tag : tags_array_map) {
            tags_map_map.put(tag, true);
        }

        // end of map

        Note note = new Note(title, description, priority, tags_list, tags_map_map);


        //noteBookRef.document("customkey").set(note);

        noteBookRef.add(note);

        //onsuccsess.......................

        ;
    }



    public void LoadNotewithPagination(View v) { // load with pagination
        Query query;

        if(lastresult == null) {
            query = noteBookRef.orderBy("priority", Query.Direction.DESCENDING).limit(2);
        }
        else {
            query = noteBookRef.orderBy("priority", Query.Direction.DESCENDING).startAfter(lastresult).limit(2);
        }

  query.get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    String data = "";

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());

                        String documentId = note.getDocumentId();
                        data +="Title: "+ note.getTitle() + "\n";
                        data +="Descr: "+ note.getDescription() + "\n";
                        data +="Priority: "+ note.getPriority() + "\n";
                        data +="DocumentId: "+ note.getDocumentId() + "\n\n";


                    }


                    if (queryDocumentSnapshots.size() == 0) {
                        textView_data_retrieved_second.setText("No more data");
                    }
                    else {
                         textView_data_retrieved_second.append(data);

                         data += "______________________________\n\n";
                        lastresult = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);

                    }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public   void LoadNote(View v){
        noteBookRef
                .whereEqualTo("maptags.tags", true)// the dot introduce nested fields in map field
//                .whereArrayContains("tags", "cv")// for spcific tags in array
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();

                            data +="ID: "+ note.getDocumentId() + "\n";

                            //for array
//                            for (String tag : note.getTags()) {
//                                data += "\n- " + tag + "\n";
//                            }
//
//                            data +="\n\n";

                            // for map

                            for (String tag : note.getMaptags().keySet()) {
                                data += "\n- " + tag + "\n";
                            }



//                            data +="Title: "+ note.getTitle() + "\n";
//                            data +="Descr: "+ note.getDescription() + "\n";
//                            data +="Priority: "+ note.getPriority() + "\n";
//                            data +="DocumentId: "+ note.getDocumentId() + "\n\n";
}

                        textView_data_retrieved_second.setText(data);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private  void  updateArray(){
//        noteBookRef.document("4WiaaIS4nxVspxKiPq4z").update("tags", FieldValue.arrayUnion("new tag")); to add a new tag to the array
        noteBookRef.document("4WiaaIS4nxVspxKiPq4z").update("tags", FieldValue.arrayRemove("new tag"));// to remove a tag from the array

    }

//######################################################################
private void executeBatchWrite() { // for doing batch write to firestore database, to do crud for more documents, here, two options, ok for all o ko for all ###################################################
    WriteBatch batch = db.batch();

    DocumentReference docRef1 = noteBookRef.document("tBJ1vLtnM1HCtGQ3ykxZ");
    batch.set(docRef1, new Note("title1", "desc1", 1, null, null));

    DocumentReference docRef2 = noteBookRef.document("Ffw7RxMuNTLi43ZelpE6");
    batch.update(docRef2,"title2", "updated Note");

    DocumentReference docRef3 = noteBookRef.document("83Z9KoxFoAjwzUUGAfwV");
    batch.delete(docRef3);

    DocumentReference docRef4 = noteBookRef.document();
    batch.set(docRef4, new Note("Added Note", "Added Note", 1, null, null));

    batch.commit().addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            textView_data_retrieved_second.setText(e.getMessage());
        }
    });

}

//######################################################################
private void executeTransaction() { // for doing transaction  to firestore database, change the value of particular field ###################################################

        db.runTransaction(new Transaction.Function<Long>() {
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentReference docRef = noteBookRef.document("tBJ1vLtnM1HCtGQ3ykxZ");
                DocumentSnapshot snapshot = transaction.get(docRef);
                long newPriority = snapshot.getLong("priority") + 1;
                transaction.update(docRef, "priority", newPriority);

                return newPriority;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            public void onSuccess(Long result) {
                Toast.makeText(Pagination_and_others.this, "Transaction success, new priority: " + result, Toast.LENGTH_SHORT).show();
            }
        });

}
}