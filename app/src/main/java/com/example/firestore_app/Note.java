package com.example.firestore_app;

import com.google.firebase.firestore.Exclude;

public class Note { // the same with firestore fields

    String title;
    String description;
    int priority;
    String documentId;


    public Note(String title, String description,int priority) {
        this.title = title;
        this.description = description;
        this.priority=priority;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }






    public Note() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
