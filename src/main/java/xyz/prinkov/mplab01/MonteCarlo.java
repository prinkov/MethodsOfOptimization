package xyz.prinkov.mplab01;

import org.mariuszgromada.math.mxparser.Function;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Random;

public class MonteCarlo extends Method {
    public static long N = 1000000;
    public static long numDothsPerThread = 5000;
    public static double[] a;
    public static double[] b;

    Random rnd = new Random();

    @Override
    public double[] min(Function f) {
        double minValue = Double.POSITIVE_INFINITY;
        double curValue = 0;

        Scope scope = Scope.create();
        Variable[] vars = new Variable[f.getArgumentsNumber()];
        double answer[] = new double[vars.length];
        double[][] doth = new double[vars.length][];

//        MonteCarlo.a = new double[]{-5, -5};
//        MonteCarlo.b = new double[]{5, 5};

        for(int i = 0; i < vars.length; i++)
            vars[i] = scope.getVariable(f.getArgument(i).getArgumentName());

        Expression expr = null;

        try {
            expr = Parser.parse(f.getFunctionExpressionString(), scope);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int varsLength = vars.length;

        long numDothsPTForCycle = numDothsPerThread - varsLength;
        long numIterate = N / numDothsPerThread;

        for(long i = 0; i < numIterate; i++) {
            for(int z = 0; z < varsLength; z++)
                doth[z] = rnd.doubles(numDothsPerThread, a[z], b[z]).toArray();

            for(int j = 0; j < numDothsPTForCycle; j += varsLength) {
                for(int k = j; k < j + vars.length; k++)
                    vars[k-j].setValue(doth[k-j][k]);

                curValue = expr.evaluate();
                if (curValue < minValue) {
                    minValue = curValue;
                    for (int k = 0; k < vars.length; k++)
                        answer[k] = vars[k].getValue();
                }
            }
        }
        return answer;
    }
}
