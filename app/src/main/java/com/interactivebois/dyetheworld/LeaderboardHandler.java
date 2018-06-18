package com.interactivebois.dyetheworld;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static android.content.ContentValues.TAG;

public class LeaderboardHandler
{
    private MapActivity view;

    private ScoreCalculationStyle scoreCalculationStyle;

    private final double earthLandmass = 148940000000000.00;

    public LeaderboardHandler(MapActivity view)
    {
        this.view = view;
    }

    public void calculateByLandmass()
    {
        double greenValue = view.getMapContainer().getTeams().get(0).getScore();
        double blueValue = view.getMapContainer().getTeams().get(1).getScore();
        double redValue = view.getMapContainer().getTeams().get(2).getScore();
        double greyValue = 100.0;

        Log.d(TAG, "calculateByLandmass: Greenvalue: " + greenValue);
        Log.d(TAG, "calculateByLandmass: Redvalue: " + redValue);
        Log.d(TAG, "calculateByLandmass: Bluevalue: " + blueValue);
        Log.d(TAG, "calculateByLandmass: Greyvalue: " + greyValue);

        double greenPercentageValue = (greenValue * 100) / earthLandmass;
        double bluePercentageValue = (blueValue * 100) / earthLandmass;
        double redPercentageValue = (redValue * 100) / earthLandmass;

        greyValue = greyValue - greenPercentageValue;
        greyValue = greyValue - bluePercentageValue;
        greyValue = greyValue - redPercentageValue;

        Log.d(TAG, "calculateByLandmass: Greenvalue: " + greenPercentageValue);
        Log.d(TAG, "calculateByLandmass: Redvalue: " + redPercentageValue);
        Log.d(TAG, "calculateByLandmass: Bluevalue: " + bluePercentageValue);
        Log.d(TAG, "calculateByLandmass: Greyvalue: " + greyValue);

        TextView greenValueText = view.findViewById(R.id.greenValue);
        TextView blueValueText = view.findViewById(R.id.blueValue);
        TextView redValueText = view.findViewById(R.id.redValue);
        TextView greyValueText = view.findViewById(R.id.greenValue);

        DecimalFormat df2 = new DecimalFormat(".##");

        greenValueText.setText(df2.format(greenPercentageValue));
        blueValueText.setText(df2.format(redPercentageValue));
        redValueText.setText(df2.format(bluePercentageValue));
        redValueText.setText(df2.format(greenValue));
    }

    public void calculateByTotalCoverage()
    {
        double greenValue = view.getMapContainer().getTeams().get(0).getScore();
        double blueValue = view.getMapContainer().getTeams().get(1).getScore();
        double redValue = view.getMapContainer().getTeams().get(2).getScore();

        double totalValue = greenValue + redValue + blueValue;

        Log.d(TAG, "calculateByLandmass: Greenvalue: " + greenValue);
        Log.d(TAG, "calculateByLandmass: Redvalue: " + redValue);
        Log.d(TAG, "calculateByLandmass: Bluevalue: " + blueValue);

        double greenPercentageValue = (greenValue * 100) / totalValue;
        double redPercentageValue = (redValue * 100) / totalValue;
        double bluePercentageValue = (blueValue * 100) / totalValue;

        Log.d(TAG, "calculateByLandmass: Greenvalue: " + greenValue);
        Log.d(TAG, "calculateByLandmass: Redvalue: " + redValue);
        Log.d(TAG, "calculateByLandmass: Bluevalue: " + blueValue);

        TextView greenValueText = view.findViewById(R.id.greenValue);
        TextView blueValueText = view.findViewById(R.id.blueValue);
        TextView redValueText = view.findViewById(R.id.redValue);

        DecimalFormat df2 = new DecimalFormat(".##");

        NumberFormat percent = NumberFormat.getPercentInstance();

        greenValueText.setText(String.valueOf(greenPercentageValue));
        blueValueText.setText(String.valueOf(bluePercentageValue));
        redValueText.setText(String.valueOf(redPercentageValue));
    }
}
