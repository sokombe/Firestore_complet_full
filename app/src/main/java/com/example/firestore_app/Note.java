package com.example.firestore_app;

import com.google.firebase.firestore.Exclude;

import java.util.List;
import java.util.Map;

public class Note { // the same with firestore fields

    String title;
    String description;
    int priority;
    String documentId;
    List<String> tags;
    Map<String,Boolean> maptags;



    public Note(String title, String description, int priority, List<String> tags, Map<String, Boolean> maptags) {
        this.title = title;
        this.description = description;
        this.priority=priority;
        this.tags=tags;
        this.maptags=maptags;
    }

    public Map<String, Boolean> getMaptags() {
        return maptags;
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


    public List<String> getTags() {
        return tags;
    }
}
