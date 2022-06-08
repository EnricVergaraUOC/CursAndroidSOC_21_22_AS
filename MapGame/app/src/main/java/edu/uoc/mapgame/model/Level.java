package edu.uoc.mapgame.model;

import java.util.ArrayList;

public class Level {
    public String name;
    public boolean unlocked;
    public ArrayList<Quiz> quizzes;

    public Level (String name, boolean unlocked){
        this.name = name;
        this.unlocked = unlocked;
        quizzes = new ArrayList<Quiz>();
    }

}
