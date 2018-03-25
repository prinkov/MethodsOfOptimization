package xyz.prinkov.mplab01.genetic;

import com.sun.javafx.binding.StringFormatter;
import parsii.eval.Expression;
import parsii.eval.Variable;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Individual implements Comparable{
    private double[] value;
    public static double[] a = {-5, -5};
    public static double[] b = {5, 5};
    boolean isNorm = false;
    public static Expression exp;
    public static Variable[] vars;

    static Random rnd = new Random();


    @Override
    public int compareTo(Object o) {
        Individual other = (Individual)o;
        other.deNorm();
        deNorm();

        for(int i = 0; i < vars.length; i++)
            vars[i].setValue(value[i]);
        double f1 = exp.evaluate();

        for(int i = 0; i < vars.length; i++)
            vars[i].setValue(other.value[i]);
        double f2 = exp.evaluate();

        norm();
        other.norm();

        return Double.compare(f1, f2);

    }

    private void norm() {
        if(!isNorm) {
            for(int i = 0; i < value.length; i++) {
                value[i] = (value[i] - a[i]) / (b[i] - a[i]);
                isNorm = true;
            }
        }
    }

    private void deNorm() {
        if(isNorm) {
            for(int i = 0; i < value.length; i++) {
                value[i] = value[i] * (b[i] - a[i]) + a[i];
                isNorm = false;
            }
        }
    }

    public Individual(int[] value) {
        this.value = IntStream.of(value).mapToDouble(Integer::valueOf).toArray();
        norm();
    }
    public Individual(double[] value) {
        this.value = DoubleStream.of(value).toArray();
        norm();
    }

    public Individual(String[] binary) {
        if(value == null)
            value = new double[binary.length];
        isNorm = true;
        setValue(binary);
    }

    public String[] getBinary() {
        int[] val = DoubleStream.of(value).mapToInt(v -> (int)(v * Integer.MAX_VALUE - 0.5))
                .toArray();
        String[] ret = new String[value.length];

        for (int i = 0; i < val.length; i++) {
            ret[i] = String.format("%31s", Integer.toBinaryString(val[i])).
                    replace(" ", "0");
        }

        return ret;
    }

    public void setValue(String[] binary) {
        int[] fromBin = Arrays.stream(binary).mapToInt(bin -> Integer.parseInt(bin, 2)).toArray();
        for(int i = 0; i < value.length; i++)
            value[i] = ((double)(fromBin[i] + 0.5) / Integer.MAX_VALUE);
    }

    public void setValue(double[] val) {
        value = DoubleStream.of(val).toArray();
        isNorm = false;
        norm();
    }

    public double[] getValue() {
        deNorm();
        double[] value_ = DoubleStream.of(value).toArray();
        norm();
        return value_;
    }

    public void mutation(double prob) {
        if(rnd.nextDouble() < prob) {
            String[] bin = getBinary();
            int i = rnd.nextInt(bin.length);
            int n = rnd.nextInt(bin[0].length());
            bin[i] = bin[i].substring(0, n) +
                    (bin[i].charAt(n) == '0' ? '1' : '0') +
                    bin[i].substring(n + 1, bin[i].length());
            setValue(bin);
        }
    }
    public static Individual coition(Individual man,
                                     Individual woman) {
        String[] manBinary = man.getBinary();
        String[] womanBinary = woman.getBinary();


        String[] childBinary = new String[womanBinary.length];
        for(int i = 0; i < manBinary.length; i++) {

            int start = rnd.nextInt(manBinary[i].length() - 3);
            int end = rnd.nextInt(manBinary[i].length() - start) + start;
            childBinary[i] = manBinary[i].substring(0, start) +
                    womanBinary[i].substring(start, end) +
                    manBinary[i].substring(end, manBinary[i].length());

        }
        return new Individual(childBinary);
    }

    @Override
    public String toString() {
        deNorm();
        String str = Arrays.toString(value);
        norm();
        return "(" + str + ")";
    }
}
