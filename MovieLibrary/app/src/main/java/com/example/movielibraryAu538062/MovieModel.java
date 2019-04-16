package com.example.movielibraryAu538062;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@Entity
public class MovieModel implements Serializable, Parcelable {
    @android.arch.persistence.room.PrimaryKey(autoGenerate = true)
    public int PrimaryKey;
    // from CSV
    public String Title;
    public String Plot;
    public String Genres;
    public String Rating;
    public int Icon;
    public String Comment;


    //User data
    public Double UserRating = null;
    public boolean Watched;
    MovieModel(String Title, String Plot, String Genres, String Rating) {
        this.Title = Title;
        this.Plot = Plot;
        this.Genres = Genres;
        this.Rating = Rating;
        IconSelector();
    }

    protected MovieModel(Parcel in) {
        PrimaryKey = in.readInt();
        Title = in.readString();
        Plot = in.readString();
        Genres = in.readString();
        Rating = in.readString();
        Icon = in.readInt();
        Comment = in.readString();
        if (in.readByte() == 0) {
            UserRating = null;
        } else {
            UserRating = in.readDouble();
        }
        Watched = in.readByte() != 0;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    private void IconSelector () {
        String PrimaryGenre = Genres.split(",")[0];

        switch (PrimaryGenre){
            case "Action":
                this.Icon = R.drawable.action;
                break;
            case "Adventure":
                this.Icon = R.drawable.adventure;

                break;
            case "Biography":
                this.Icon = R.drawable.biography;
                break;
            case "Comedy":
                this.Icon = R.drawable.comedy;
                break;
            case "Drama":
                this.Icon = R.drawable.drama;
                break;
            case "Fantasy":
                this.Icon = R.drawable.fantasy;
                break;
            case "History":
                this.Icon = R.drawable.history;
                break;
            case "Music":
                this.Icon = R.drawable.music;
                break;
            case "Romance":
                this.Icon = R.drawable.romance;
                break;
            case "Sci-fi":
                this.Icon = R.drawable.scifi;
                break;
            case "Sport":
                this.Icon = R.drawable.sport;
                break;
            case "Thriller":
                this.Icon = R.drawable.thriller;
                break;

            case "Animation":
                this.Icon = R.drawable.animation;
                break;
            default:
                this.Icon = R.drawable.ic_launcher_foreground;
                break;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PrimaryKey);
        dest.writeString(Title);
        dest.writeString(Plot);
        dest.writeString(Genres);
        dest.writeString(Rating);
        dest.writeInt(Icon);
        dest.writeString(Comment);
        if (UserRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(UserRating);
        }
        dest.writeByte((byte) (Watched ? 1 : 0));
    }
}
