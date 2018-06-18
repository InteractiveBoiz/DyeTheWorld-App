package com.interactivebois.dyetheworld.Model;

import java.util.Comparator;

public class Team implements Comparator<Team>
{
    private String id;
    private String colour;
    private double score;

    public Team(String id, String colour)
    {
        this.id = id;
        this.colour = colour;
    }

    public Team(String id, String colour, double score)
    {
        this.id = id;
        this.colour = colour;
        this.score = score;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getColour()
    {
        return colour;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    @Override
    public int compare(Team o1, Team o2)
    {
        if(o1.getId() == o2.getId())
            return 0;
        else
            return 1;
    }
}

