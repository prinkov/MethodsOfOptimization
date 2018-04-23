package xyz.prinkov.mplab01.interval;

import org.mariuszgromada.math.mxparser.Function;
import xyz.prinkov.mplab01.Annealing;
import xyz.prinkov.mplab01.MonteCarlo;

public class Bar {
    public double[][] intervals;
    public double valMin;

    public Bar(double[] left, double[] right) {
        intervals = new double[left.length][];
        for(int i = 0; i < left.length; i++) {
            intervals[i] = new double[2];
            intervals[i][0] = left[i];
            intervals[i][1] = right[i];
        }
    }


    public double[] getBound(int ind) {
        double[] ret = new double[intervals.length];
        for(int i = 0; i < intervals.length; i++)
            ret[i] = intervals[i][ind];
        return ret;
    }

    public double[] getLeftBound() {
        return getBound(0);
    }

    public double[] getRightBound() {
        return getBound(1);
    }

    public double[] wid() {
        double[] wid = new double[intervals.length];
        for(int i = 0; i < intervals.length; i++)
            wid[i] = Math.abs(intervals[i][0] - intervals[i][1]);
        return wid;
    }

    public Bar(Bar other) {
        intervals = new double[other.intervals.length][];
        for(int i = 0; i < intervals.length; i++) {
            intervals[i] = new double[2];
            intervals[i][0] = other.intervals[i][0];
            intervals[i][1] = other.intervals[i][1];
        }
        valMin = other.valMin;
    }

    public double mid(int component) {
        return (intervals[component][1] - intervals[component][0]) / 2.0
                + intervals[component][0];
    }

    public Bar[] bisection(int component) {
        Bar[] newBar = new Bar[2];
        newBar[0] = new Bar(this);
        newBar[1] = new Bar(this);

        newBar[0].intervals[component][1] = newBar[0].mid(component);
        newBar[1].intervals[component][0] = newBar[1].mid(component);

        return newBar;
    }

    public int argMaxWid() {
        int argMax = -1;
        double max = Double.NEGATIVE_INFINITY;
        double[] wids = wid();
        for(int i = 0; i < wids.length; i++) {
            if(wids[i] > max) {
                max = wids[i];
                argMax = i;
            }
        }
        return argMax;
    }

    public double[] getValue() {
        double[] val = new double[intervals.length];
        for(int i = 0; i < intervals.length; i++)
            val[i] = intervals[i][0];
        return val;
    }

    @Override
    public String toString() {
        String ret = "{";
        for(int i = 0; i < intervals.length; i++)
            ret += "[" + intervals[i][0] + "," + intervals[i][1] + "]x";
        ret = ret.substring(0, ret.length() - 1);
        return ret + "}";
    }
}
