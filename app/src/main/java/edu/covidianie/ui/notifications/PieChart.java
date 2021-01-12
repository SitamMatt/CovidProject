package edu.covidianie.ui.notifications;

import androidx.annotation.Nullable;

import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.PieOption;

public class PieChart implements IPieInfo {

    private double value;
    private int color;
    private PieOption mPieOption;
    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String getDesc() {
        return "PieChart";
    }

    @Nullable
    @Override
    public PieOption getPieOption() {
        // option for pie,example: descript label
        return mPieOption;
    }
}
