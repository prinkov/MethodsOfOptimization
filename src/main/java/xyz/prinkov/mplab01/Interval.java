package xyz.prinkov.mplab01;

import org.mariuszgromada.math.mxparser.Function;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;
import xyz.prinkov.mplab01.interval.Bar;
import xyz.prinkov.mplab01.interval.Estimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Interval extends Method {
    public static double eps = 0.001;
    public static Bar lastBar;
    public static double[] a;
    public static double[] b;

    @Override
    public double[] min(Function f) throws ParseException {

        Scope scope = Scope.create();
        int varsLength = f.getArgumentsNumber();
        Variable[] vars = new Variable[varsLength];
        final Expression expr =  Parser.parse(f.getFunctionExpressionString(), scope);

        for(int i = 0; i < vars.length; i++) {
            vars[i] = scope.getVariable(f.getArgument(i).getArgumentName());
            vars[i].setValue(a[i]);
        }

        ArrayList<Bar> bars = new ArrayList<>();

        Bar p = new Bar(a, b);

        Estimator estimator = new Estimator(f);
        p.valMin = f.calculate(estimator.compute(p));

        bars.add(p);
        while(Arrays.stream(bars.get(0).wid()).max().orElse(0) > eps) {
            int component = bars.get(0).argMaxWid();
            Bar[] bisP = bars.get(0).bisection(component);

            double[] temp = estimator.compute(bisP[0]);

            for(int i = 0; i < vars.length; i++)
                vars[i].setValue(temp[i]);
            double min_est = expr.evaluate();
            temp = bisP[0].getValue();
            for(int i = 0; i < vars.length; i++)
                vars[i].setValue(temp[i]);
            double min_bound = expr.evaluate();

            bisP[0].valMin = Math.min(min_bound, min_est);

            temp = estimator.compute(bisP[1]);
            for(int i = 0; i < vars.length; i++)
                vars[i].setValue(temp[i]);
             min_est = expr.evaluate();
            temp = bisP[1].getValue();
            for(int i = 0; i < vars.length; i++)
                vars[i].setValue(temp[i]);
            min_bound = expr.evaluate();

            bisP[1].valMin = Math.min(min_bound, min_est);


            bars.add(new Bar(bisP[0]));
            bars.add(new Bar(bisP[1]));


            bars.remove(0);
            bars.sort(new Comparator<Bar>() {
                @Override
                public int compare(Bar bar, Bar t1) {
                    return Double.compare(bar.valMin, t1.valMin);
                }
            });
        }

        System.out.println(bars.get(0));
        double[] answer = bars.get(0).getValue();
        lastBar = new Bar(bars.get(0));
        return answer;
    }
}
