package xyz.prinkov.mplab01;

import org.mariuszgromada.math.mxparser.Function;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Random;

public class MonteCarlo extends Method {
    public static int N = 1000000;

    int a = -5;
    int b = 5;

    Random rnd = new Random();

    @Override
    public double[] min(Function f) {

        double minValue = Double.POSITIVE_INFINITY;
        double[] doth = null;
        double curValue = 0;

        Scope scope = Scope.create();
        Variable[] vars = new Variable[f.getArgumentsNumber()];
        double answer[] = new double[vars.length];


        for(int i = 0; i < vars.length; i++)
            vars[i] = scope.getVariable(f.getArgument(i).getArgumentName());

        Expression expr = null;

        try {
            expr = Parser.parse(f.getFunctionExpressionString(), scope);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int varsLength = vars.length;

        int numDothsPerThread = 5000;
        int numDothsPTForCycle = numDothsPerThread - varsLength;
        int numIterate = N / numDothsPerThread;

        for(int i = 0; i < numIterate; i++) {
            doth = rnd.doubles(numDothsPerThread,-5 ,5).toArray();

            for(int j = 0; j < numDothsPerThread; j += varsLength) {
                for(int k = j; k < j + vars.length; k++)
                    vars[k-j].setValue(doth[k]);

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
