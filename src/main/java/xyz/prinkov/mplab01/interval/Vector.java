package xyz.prinkov.mplab01.interval;

import java.util.Arrays;

public class Vector {
    double values[];

    public Vector(double[] values) {
        this.values = Arrays.copyOfRange(values, 0, values.length);
    }

    public Vector(Vector vector) {
        this.values = Arrays.copyOfRange(vector.values, 0, vector.values.length);
    }

    public Vector(int n) {
        this.values = new double[n];
        for(int i = 0; i < n; i++)
            values[i] = 0;
    }


    public double get(int n) {
        return values[n];
    }

    public int size() {
        return values.length;
    }

    public Vector add(int index, double val) {
        Vector newVector = new Vector(this);
        newVector.values[index] += val;
        return newVector;
    }

    public boolean equals(Vector other) {
        for(int  i = 0; i < values.length; i++)
            if(this.values[i] != other.values[i])
                return false;
        return true;
    }

    public Vector minus(Vector other) {
        Vector ret = new Vector(this);
        for(int i = 0; i < ret.size(); i++)
            ret.values[i] -= other.values[i];
        return ret;
    }

    public Vector abs() {
        Vector ret = new Vector(this);
        for(int i = 0; i < ret.size(); i++)
            ret.values[i] = Math.abs(ret.values[i]);
        return ret;
    }

    public boolean isLess(double val) {
        for(int i = 0; i < this.size(); i++)
            if(this.values[i] < val)
                continue;
            else
                return false;
        return true;
    }


    public Vector plus(Vector other) {
        Vector ret = new Vector(this);
        for(int i = 0; i < ret.size(); i++)
            ret.values[i] += other.values[i];
        return ret;
    }


    public double mult(Vector other) {
        double res = 0;
        for(int i = 0; i < this.size(); i++)
            res += this.values[i] * other.values[i];
        return res;
    }

    double norm() {
        double sum = 0;
        for(int i = 0; i < this.size(); i++) {
            sum += values[i] * values[i];
        }
        return Math.sqrt(sum);
    }

    public Vector mult(double coef) {
        Vector ret = new Vector(this);
        for(int i = 0; i < ret.size(); i++)
            ret.values[i] *= coef;
        return ret;
    }

    public double[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        String ret = "(";

        for(int i = 0; i < values.length; i++) {
            ret += values[i];
            if(i < values.length - 1)
                ret += ";";
        }
        ret += ")";
        return ret;
    }
}
