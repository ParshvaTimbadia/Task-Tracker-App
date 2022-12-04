//package edu.northeastern.numad22fa_team51_project.models;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import org.checkerframework.checker.nullness.qual.NonNull;
//
//import java.util.ArrayList;
//
//public class Board implements Parcelable {
//
//
//    private String name = "";
//    private String image = "";
//    private String creadedBy = "";
//    private ArrayList<String> assingedTo = new ArrayList<>();
////    private String documentId = "";
////    private ArrayList<Task> taskList = new ArrayList<>();
//
//
//    public Board(@NonNull Parcel in) {
//        name = in.readString();
//        image = in.readString();
//        creadedBy = in.readString();
//        assingedTo = in.createStringArrayList();
////        documentId = in.readString();
////        taskList = in.createTypedArrayList(Task.CREATOR);
//    }
//
//    public static final Creator<Board> CREATOR = new Creator<Board>() {
//        @Override
//        public Board createFromParcel(Parcel in) {
//            return new Board(in);
//        }
//
//        @Override
//        public Board[] newArray(int size) {
//            return new Board[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(name);
//        parcel.writeString(image);
//        parcel.writeString(creadedBy);
//        parcel.writeStringList(assingedTo);
////        parcel.writeString(documentId);
////        parcel.writeTypedList(taskList);
//    }
//}
