package edu.northeastern.numad22fa_team51_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.checkerframework.checker.nullness.qual.NonNull;

public class Task implements Parcelable {

    private String title = "";
    private String createdBy = "";

    protected Task(@NonNull Parcel in) {
        title = in.readString();
        createdBy = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(createdBy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
