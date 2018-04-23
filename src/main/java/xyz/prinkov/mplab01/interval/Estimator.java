package xyz.prinkov.mplab01.interval;

import org.mariuszgromada.math.mxparser.Function;

public class Estimator {
    double delta = 1;
    double lambda = 2;
    static double eps = 0.00001;

    Function f;
    public Estimator(Function f) {
        delta = 1;
        lambda = 2;
        this.f = f;
    }

    private Vector exploringSearch(Vector vector) {
        for(int i = 0; i < vector.size(); i++)
            if(f.calculate(vector.add(i, delta).getValues())
                    < f.calculate(vector.getValues()))
                vector = vector.add(i, delta);
            else if(f.calculate(vector.add(i, -delta).getValues())
                    < f.calculate(vector.getValues()))
                vector = vector.add(i, -delta);
        return vector;
    }

    public double[] compute(Bar bar) {
        double[] a = bar.getLeftBound();
        double[] b = bar.getRightBound();
        double[] vals = new double[bar.getLeftBound().length];
        for(int i = 0; i < vals.length; i++)
            vals[i] =  bar.mid(i);

        Vector x = new Vector(vals);
        delta = 1;
        lambda = 2;

        boolean flag;

        while(delta > eps) {
            flag = true;
            while(flag) {
                Vector y = exploringSearch(x);
                if(x.equals(y))
                    break;
                while(f.calculate(y.getValues()) < f.calculate(x.getValues())) {
                    Vector obr = y.minus(x);

                    double[] vals_ = y.getValues();

                    for(int i = 0; i < vals_.length; i++)
                        if (vals_[i] > b[i] || vals_[i] < a[i])
                            flag = false;
                    if(!flag)
                        break;

                    x = new Vector(y);
                    y = x.plus(obr.mult(lambda - 1));
                    y = exploringSearch(y);
                }
                if(!flag)
                    continue;
            }
            delta /= 2.0;
        }

        return x.getValues();
    }
}
