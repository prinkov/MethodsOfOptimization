package xyz.prinkov.mplab01;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Function;
import org.omg.CORBA.TRANSIENT;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {
    static public  ArrayList<VarBox> vars;
    static public ProgressIndicator pbMC;
    static  Font mainFont = Font.font("Monaco", FontWeight.BOLD, 22);
    static VBox variables;
    static java.awt.Color TRANSPARENT = java.awt.Color.WHITE;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Function[] targetFunction = new Function[1];

        MyFunctionTextField targetFText = new MyFunctionTextField();
        targetFText.setText("f(x, y) = 4*x^2 - 2.1*x^4 +x^6/3 + x*y-4*y^2+4*y^4");
        TeXFormula texFormula = new TeXFormula("\\to \\min");
        Image t = (SwingFXUtils.toFXImage((BufferedImage) texFormula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 30,
                java.awt.Color.BLACK, TRANSPARENT), null));

        VBox vboxMain = new VBox();

        vboxMain.setSpacing(15);
        vboxMain.setPadding(new Insets(5,5,5,5));
        vboxMain.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        HBox functionLine = new HBox();
        functionLine.setAlignment(Pos.CENTER);
        functionLine.setSpacing(0);
        functionLine.setFillHeight(true);
        functionLine.getChildren().addAll(targetFText, new ImageView(t));
        vboxMain.getChildren().addAll(functionLine);

        ScrollPane varSc = new ScrollPane();
        varSc.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        varSc.setMaxHeight(100);
        varSc.setFitToHeight(true);
        varSc.setFitToWidth(true);


        variables = new VBox();
        varSc.setContent(variables);
        varSc.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        variables.setAlignment(Pos.CENTER);
        variables.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));



        vars = new ArrayList<>();

        targetFunction[0] = new Function(targetFText.getText());

        for(int i = 0; i < targetFunction[0].getArgumentsNumber(); i++)
            vars.add(new VarBox(targetFunction[0].getParameterName(i)));

        variables.getChildren().addAll(vars);

        TabPane tabPane = new TabPane();
        VBox monteCarloBox = new VBox();
        VBox annealingBox = new VBox();
        VBox geneticBox = new VBox();
        VBox intervalBox = new VBox();
        VBox intervalGeneticBox = new VBox();

        monteCarloBox.setAlignment(Pos.BOTTOM_CENTER);
        monteCarloBox.setPadding(new Insets(10,10,10,10));

        HBox inputMC = new HBox();
        inputMC.setAlignment(Pos.CENTER);


        TextField countDotsMC = new TextField("1000000");
        Button startMC =  new Button("Посчитать");
        Label timeLbl = new Label();
        pbMC = new ProgressIndicator();
        pbMC.setVisible(false);
        TextArea outputMC = new TextArea();

        int[] time = {0, 0, 0};

        timeLbl.setText("0:0:0");
        timeLbl.setMinWidth(70);

        Timeline timeline = new Timeline (
                new KeyFrame(
                        Duration.millis(10),
                        ae -> {
                            time[2]++;
                            timeLbl.setText(time[0] + ":" + time[1] + ":" +  time[2] * 10 + "");
                        }
                )
        );
        timeline.setCycleCount(99);
        timeline.stop();


        timeline.setOnFinished(e->{
            time[1]++;
            time[2]=0;
            if(time[1] == 60) {
                time[1] = 0;
                time[0]++;
            }
            timeline.play();
        });

        countDotsMC.setMaxWidth(100);

        inputMC.getChildren().addAll(new Label("Число генерируемых точек: "),
                countDotsMC);
        inputMC.setSpacing(5);
        outputMC.setEditable(false);
        monteCarloBox.setSpacing(5);

        HBox timeMCBox = new HBox();
        timeMCBox.setAlignment(Pos.CENTER);


        monteCarloBox.getChildren().addAll(inputMC, startMC, timeMCBox, outputMC);

        HBox firstLine = new HBox();
        HBox secondLine = new HBox();

        firstLine.setAlignment(Pos.CENTER);
        secondLine.setAlignment(Pos.CENTER);
        firstLine.setSpacing(10);
        secondLine.setSpacing(10);
        firstLine.setPadding(new Insets(5));
        secondLine.setPadding(new Insets(5));

        Label maxTempLbl = new Label("Tmax: ");
        Label countCycleLbl = new Label("L: ");
        Label tempDecLbl = new Label("R: ");
        Label epsLbl = new Label("Eps:     ");

        TextField maxTempTF = new TextField("1000000");
        TextField countCycleTF = new TextField("70");
        TextField tempDecTF = new TextField("0.9");
        TextField epsTF = new TextField("0.01");


        final Tooltip maxTempTooltip = new Tooltip();
        final Tooltip countCycleTooltip = new Tooltip();
        final Tooltip tempDecTooltip = new Tooltip();
        final Tooltip epsTooltip = new Tooltip();
        maxTempTooltip.setText("Максимальна температура");
        countCycleTooltip.setText("Количество циклов");
        tempDecTooltip.setText("Параметр снижения температуры");
        epsTooltip.setText("Эпсилон окрестность");

        maxTempLbl.setTooltip(maxTempTooltip);
        maxTempTF.setTooltip(maxTempTooltip);
        countCycleLbl.setTooltip(countCycleTooltip);
        countCycleTF.setTooltip(countCycleTooltip);
        tempDecLbl.setTooltip(tempDecTooltip);
        tempDecTF.setTooltip(tempDecTooltip);
        epsLbl.setTooltip(epsTooltip);
        epsTF.setTooltip(epsTooltip);


        firstLine.getChildren().addAll(
            maxTempLbl,
            maxTempTF,
            countCycleLbl,
            countCycleTF
        );
        secondLine.getChildren().addAll(
            epsLbl,
            epsTF,
            tempDecLbl,
            tempDecTF

        );

        Button startA = new Button("Посчитать");

        TextArea outA = new TextArea();
        outA.setEditable(true);


        annealingBox.setAlignment(Pos.BOTTOM_CENTER);
        annealingBox.setSpacing(5);
        annealingBox.getChildren().addAll(firstLine, secondLine, startA,
                outA);


        geneticBox.setAlignment(Pos.BOTTOM_CENTER);
        geneticBox.setSpacing(5);
        Button startG = new Button("Посчитать");
        TextArea outG = new TextArea();
        outG.setEditable(true);

        HBox firstG = new HBox();
        firstG.setAlignment(Pos.CENTER);
        firstG.setPadding(new Insets(10));
        firstG.setSpacing(10);

        HBox secondG = new HBox();
        secondG.setAlignment(Pos.CENTER);
        secondG.setPadding(new Insets(10));
        secondG.setSpacing(10);

        TextField numOfEpoch = new TextField("3000");
        TextField sizePop = new TextField("320");
        TextField prob = new TextField("0.1");
        numOfEpoch.setMaxWidth(100);
        sizePop.setMaxWidth(100);
        prob.setMaxWidth(100);

        firstG.getChildren().addAll(
                new Label("Количество эпох:"),
                numOfEpoch,
                new Label(" Размер популяции: "),
                sizePop
                );

        secondG.getChildren().addAll(
                new Label(" Вероятность мутации: "), prob);

        geneticBox.getChildren().addAll(
                firstG,
                secondG,
                startG,
                outG);


        intervalBox.setAlignment(Pos.BOTTOM_CENTER);
        intervalBox.setSpacing(5);
        Button startI = new Button("Посчитать");
        TextArea outI = new TextArea();
        TextField inputEps = new TextField();
        HBox intervalTextLine = new HBox();
        intervalTextLine.setAlignment(Pos.CENTER);
        intervalBox.setSpacing(15);
        inputEps.setMaxWidth(80);
        inputEps.setText("0.001");

        intervalBox.setPadding(new Insets(10,10,10,10));

        intervalTextLine.getChildren().addAll(new Label("Точность: "), inputEps);

        intervalBox.getChildren().addAll(intervalTextLine, startI, outI);

        intervalGeneticBox.setAlignment(Pos.BOTTOM_CENTER);
        intervalGeneticBox.setSpacing(5);
        Button startIG = new Button("Посчитать");
        TextArea outIG = new TextArea();
        TextField inputEpsIG = new TextField();
        HBox intervalGTextLine = new HBox();
        intervalGTextLine.setAlignment(Pos.CENTER);
        intervalGeneticBox.setSpacing(15);
        inputEpsIG.setMaxWidth(80);
        inputEpsIG.setText("0.001");

        intervalGeneticBox.setPadding(new Insets(10,10,10,10));

        intervalGTextLine.getChildren().addAll(new Label("Точность: "), inputEpsIG);

        intervalGeneticBox.getChildren().addAll(intervalGTextLine, startIG, outIG);

        startMC.setOnMouseClicked(e-> {
            StringBuffer str = new StringBuffer();
            startMC.setDisable(true);
            startA.setDisable(true);
            startG.setDisable(true);
            startI.setDisable(true);
            startIG.setDisable(true);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    timeLbl.setText("0:0:0");
                    outputMC.setText("");

                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            time[0] = 0;
                            time[1] = 0;
                            time[2] = 0;
                            Function f = new Function(targetFText.getText());
                            MonteCarlo mc = new MonteCarlo();
                            MonteCarlo.N = Integer.parseInt(countDotsMC.getText());

                            MonteCarlo.a = new double[vars.size()];
                            MonteCarlo.b = new double[vars.size()];

                            for(int i = 0; i < vars.size(); i ++) {
                                MonteCarlo.a[i] = vars.get(i).leftValue;
                                MonteCarlo.b[i] = vars.get(i).rightValue;
                            }

                            System.out.println(Arrays.toString(MonteCarlo.a));
                            System.out.println(Arrays.toString(MonteCarlo.b));

                            timeline.play();
                            double[] min = mc.min(f);
                            timeline.stop();
                            str.append("Минимальное значение функции fmin = "+ f.calculate(min) +"\n");
                            str.append("В точке arg(fmin)= " + Arrays.toString(min) + "\n");
                            str.append("Время выполнения алгоритма " + time[0] + " мин., "+
                                    time[1] + " сек., " + time[2]*10 + " мс. \n");

                            return null;
                        }
                    };

                }
            };
            service.start();
            service.setOnSucceeded(ee-> {
                startMC.setDisable(false);
                startA.setDisable(false);
                startG.setDisable(false);
                startI.setDisable(false);
                startIG.setDisable(false);
                outputMC.setText(str.toString());
            });

        });

        startA.setOnMouseClicked(e-> {
            StringBuffer str = new StringBuffer();
            startMC.setDisable(true);
            startA.setDisable(true);
            startG.setDisable(true);
            startI.setDisable(true);
            startIG.setDisable(true);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    timeLbl.setText("0:0:0");
                    outA.setText("");

                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            time[0] = 0;
                            time[1] = 0;
                            time[2] = 0;

                            Function f = new Function(targetFText.getText());


                            Annealing ann = new Annealing();

                            Annealing.Tmax = Float.parseFloat(maxTempTF.getText());
//
                            Annealing.eps = Float.parseFloat(epsTF.getText());
                            Annealing.r = Float.parseFloat(tempDecTF.getText());
                            Annealing.L = Integer.parseInt(countCycleTF.getText());

                            System.out.println(vars);
                            System.out.println(f.toString());

                            Annealing.a = new double[vars.size()];
                            Annealing.b = new double[vars.size()];


                            for(int i = 0; i < vars.size(); i ++) {
                                Annealing.a[i] = vars.get(i).leftValue;
                                Annealing.b[i] = vars.get(i).rightValue;
                            }

                            timeline.play();
                            double[] min = ann.min(f);
                            timeline.stop();
                            str.append("Минимальное значение функции fmin = "+ f.calculate(min) +"\n");
                            str.append("В точке arg(fmin)= " + Arrays.toString(min) + "\n");
                            str.append("Время выполнения алгоритма " + time[0] + " мин., "+
                                    time[1] + " сек., " + time[2] * 10 + " мс. \n");

                            return null;
                        }
                    };

                }
            };
            service.start();
            service.setOnSucceeded(ee-> {
                startMC.setDisable(false);
                startA.setDisable(false);
                startG.setDisable(false);
                startI.setDisable(false);
                startIG.setDisable(false);
                outA.setText(str.toString());
            });

        });

        startG.setOnMouseClicked(e-> {
            StringBuffer str = new StringBuffer();
            startMC.setDisable(true);
            startA.setDisable(true);
            startG.setDisable(true);
            startI.setDisable(true);
            startIG.setDisable(true);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    timeLbl.setText("0:0:0");
                    outG.setText("");

                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            time[0] = 0;
                            time[1] = 0;
                            time[2] = 0;

                            Function f = new Function(targetFText.getText());

                            Genetic genetic = new Genetic();



                            Genetic.a = new double[vars.size()];
                            Genetic.b = new double[vars.size()];


                            for(int i = 0; i < vars.size(); i ++) {
                                Genetic.a[i] = vars.get(i).leftValue;
                                Genetic.b[i] = vars.get(i).rightValue;
                            }



                            Genetic.numOfEpoch = Integer.parseInt(numOfEpoch.getText());
                            Genetic.k = Integer.parseInt(sizePop.getText());
                            Genetic.probMutation = Double.parseDouble(prob.getText());

                            timeline.play();
                            double[] min = null;
                            try {
                                min = genetic.min(f);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            timeline.stop();
                            str.append("Минимальное значение функции fmin = "+ f.calculate(min) +"\n");
                            str.append("В точке arg(fmin)= " + Arrays.toString(min) + "\n");
                            str.append("Время выполнения алгоритма " + time[0] + " мин., "+
                                    time[1] + " сек., " + time[2] * 10 + " мс. \n");

                            return null;
                        }
                    };

                }
            };
            service.start();
            service.setOnSucceeded(ee-> {
                startMC.setDisable(false);
                startA.setDisable(false);
                startG.setDisable(false);
                startI.setDisable(false);
                startIG.setDisable(false);
                outG.setText(str.toString());
            });

        });

        startI.setOnMouseClicked(e-> {
            StringBuffer str = new StringBuffer();
            startMC.setDisable(true);
            startA.setDisable(true);
            startG.setDisable(true);
            startI.setDisable(true);
            startIG.setDisable(true);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    timeLbl.setText("0:0:0");
                    outG.setText("");

                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            time[0] = 0;
                            time[1] = 0;
                            time[2] = 0;

                            Function f = new Function(targetFText.getText());

                            Interval interval = new Interval();

                            Interval.a = new double[vars.size()];
                            Interval.b = new double[vars.size()];


                            for(int i = 0; i < vars.size(); i ++) {
                                Interval.a[i] = vars.get(i).leftValue;
                                Interval.b[i] = vars.get(i).rightValue;
                            }

                            Interval.eps = Double.parseDouble(inputEps.getText());
                            Interval.flag = false;

                            timeline.play();
                            double[] min = null;
                            try {
                                min = interval.min(f);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            timeline.stop();
                            str.append("Минимальное значение функции fmin = "+ (f.calculate(min)) +"\n");
                            str.append("В точке arg(fmin)= " + Arrays.toString(min) + "\n");
                            str.append("Точка минимума лежит в брусе: " + Interval.lastBar + "\n");
                            str.append("Время выполнения алгоритма " + time[0] + " мин., "+
                                    time[1] + " сек., " + time[2] * 10 + " мс. \n");

                            return null;
                        }
                    };

                }
            };
            service.start();
            service.setOnSucceeded(ee-> {
                startMC.setDisable(false);
                startA.setDisable(false);
                startG.setDisable(false);
                startI.setDisable(false);
                startIG.setDisable(false);
                outI.setText(str.toString());
            });

        });

        startIG.setOnMouseClicked(e-> {
            StringBuffer str = new StringBuffer();
            startMC.setDisable(true);
            startA.setDisable(true);
            startG.setDisable(true);
            startI.setDisable(true);
            startIG.setDisable(true);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    timeLbl.setText("0:0:0");
                    outputMC.setText("");

                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            time[0] = 0;
                            time[1] = 0;
                            time[2] = 0;
                            Function f = new Function(targetFText.getText());

                            Interval interval = new Interval();

                            Interval.a = new double[vars.size()];
                            Interval.b = new double[vars.size()];


                            for(int i = 0; i < vars.size(); i ++) {
                                Interval.a[i] = vars.get(i).leftValue;
                                Interval.b[i] = vars.get(i).rightValue;
                            }

                            Interval.eps = Double.parseDouble(inputEps.getText());
                            Interval.flag = true;

                            timeline.play();
                            double[] min = null;
                            try {
                                min = interval.min(f);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            MonteCarlo mc = new MonteCarlo();
                            MonteCarlo.N = Integer.parseInt(countDotsMC.getText());
                            MonteCarlo.a = new double[vars.size()];
                            MonteCarlo.b = new double[vars.size()];
                            for(int i = 0; i < vars.size(); i ++) {
                                MonteCarlo.a[i] = Interval.lastBar.getLeftBound()[i];
                                MonteCarlo.b[i] = Interval.lastBar.getRightBound()[i];
                            }

                            min = mc.min(f);

                            timeline.play();
                            timeline.stop();
                            str.append("Минимальное значение функции fmin = "+ f.calculate(min) +"\n");
                            str.append("В точке arg(fmin)= " + Arrays.toString(min) + "\n");
                            str.append("Время выполнения алгоритма " + time[0] + " мин., "+
                                    time[1] + " сек., " + time[2]*10 + " мс. \n");

                            return null;
                        }
                    };

                }
            };
            service.start();
            service.setOnSucceeded(ee-> {
                startMC.setDisable(false);
                startA.setDisable(false);
                startG.setDisable(false);
                startI.setDisable(false);
                startIG.setDisable(false);
                outIG.setText(str.toString());
            });

        });


        Tab tabMC = new Tab();
        Tab tabAS =new Tab();
        Tab tabG =new Tab();
        Tab tabI =new Tab();
        Tab tabIG =new Tab();

        tabMC.setContent(monteCarloBox);
        tabMC.setText("Метод Монте-Карло");
        tabAS.setContent(annealingBox);
        tabAS.setText("Метод имитация отжига");
        tabG.setContent(geneticBox);
        tabG.setText("Генетические алгоритмы");
        tabI.setContent(intervalBox);
        tabI.setText("Интервальный метод");
        tabIG.setContent(intervalGeneticBox);
        tabIG.setText("Интервально-генетиеский метод");


        tabMC.setClosable(false);
        tabAS.setClosable(false);
        tabG.setClosable(false);
        tabI.setClosable(false);
        tabI.setClosable(false);
        tabIG.setClosable(false);
        tabPane.getTabs().addAll(tabMC, tabAS, tabG, tabI, tabIG);

        HBox timeBox = new HBox();
        timeBox.setSpacing(5);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.getChildren().addAll(new Label("Время выполнения: "), timeLbl);

        vboxMain.getChildren().addAll(varSc, tabPane, new Separator(),  timeBox);
        tabPane.setTabMinWidth(250);


        Scene scene = new Scene(vboxMain);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            tabPane.setTabMinWidth(newValue.floatValue() / 3.0 - 30);
            tabPane.setTabMaxWidth(newValue.floatValue() / 3.0 - 30);
        };

        stage.widthProperty().addListener(stageSizeListener);
        vboxMain.setMaxWidth(600);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    static class MyLabel extends Label {
        public MyLabel(String t) {
            super(t);
            Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);

            setAlignment(Pos.CENTER);
            setFont(mainFont);
            setPadding(new Insets(10,10,10,10));
        }
    }

    static class MyTextField extends TextField {
        public MyTextField() {
            super();
            setMinWidth(410);
            Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);
            setFont(mainFont);

            setText("f(x,y,z) = 2*cos(y)");

            setPromptText("f(x,y) = x+y");
            setFocusTraversable(false);
        }
    }

    static class MyFunctionTextField extends TextField {
        public MyFunctionTextField() {
            super();
            setMinWidth(410);
            Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);
            setFont(mainFont);

            setText("f(x,y,z) = 2*cos(y)");

            setPromptText("f(x,y) = x+y");
            setFocusTraversable(false);
            Function[] f = new Function[1];

            javafx.event.EventHandler e = event -> {
                f[0] = new Function(getText());
                try {
                    double[] testArr = new double[f[0].getArgumentsNumber()];
                    for(int i = 0; i < testArr.length; i++)
                        testArr[i] = 0.1;
                    if (Double.isNaN(f[0].calculate(new double[f[0].getArgumentsNumber()]))
                            && Double.isNaN(f[0].calculate(testArr))
                            ) {
                        setStyle(" -fx-faint-focus-color: transparent;\n" +
                                "-fx-border-color:rgba(255,0,0,0.9);" +
                                "-fx-background-color: rgba(255,0,0,0.2)");
                    } else {
                        setStyle("");
                        setStyle(" -fx-faint-focus-color: rgba(0, 255, 0, 0.8);\n");

                        if(vars.size() != f[0].getArgumentsNumber()) {
                            variables.getChildren().removeAll(vars);
                            if (vars.size() > f[0].getArgumentsNumber())
                                for (int i = 0; i < vars.size(); i++)
                                    for (int j = 0; j < f[0].getArgumentsNumber(); j++) {
                                        if (vars.get(i).name.compareTo(f[0].getArgument(j).getArgumentName()) == 0)
                                            break;
                                        if(j == f[0].getArgumentsNumber() - 1)
                                            vars.remove(i);
                                }
                            if (vars.size() < f[0].getArgumentsNumber())
                                for (int i = 0; i < f[0].getArgumentsNumber(); i++)
                                    for (int j = 0; j < vars.size(); j++){
                                        if (vars.get(j).name.compareTo(f[0].getArgument(i).getArgumentName()) == 0)
                                            break;
                                        if(j == vars.size() - 1)
                                            vars.add(new VarBox(f[0].getArgument(i).getArgumentName()));
                                }
                                variables.getChildren().addAll(vars);
                        }

                        System.out.println(vars.toString());

                    }
                } catch (Exception exp) {
                    setStyle(" -fx-faint-focus-color: transparent;\n" +
                            "-fx-border-color:rgba(255,0,0,0.9);" +
                            "-fx-background-color: rgba(255,0,0,0.2)");
                }
            };
            setOnKeyReleased(e);
            setOnMouseClicked(e);
        }
    }


    static class VarBox extends HBox {
        public double leftValue = 0;

        public double rightValue = 1;

        public String name;

        @Override
        public String toString() {
            return name;
        }

        public VarBox(String varName) {
            super();


            name = varName;
            this.setAlignment(Pos.CENTER);
            this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                    CornerRadii.EMPTY, Insets.EMPTY)));
            TeXFormula texFormula = new TeXFormula(varName + " \\in");
            Image t = (SwingFXUtils.toFXImage((BufferedImage)
                    texFormula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 30,
                    java.awt.Color.BLACK, TRANSPARENT), null));
            Label dotcom = new Label(";");

            dotcom.setFont(mainFont);

            TextField left = new TextField();
            TextField right = new TextField();

            left.setMaxWidth(50);
            right.setMaxWidth(50);

            left.setText(leftValue + "");
            right.setText(rightValue + "");

            left.setFocusTraversable(false);
            right.setFocusTraversable(false);


            javafx.event.EventHandler e = event -> {
                try {
                    Double.parseDouble(((TextField) event.getSource()).getText());
                    ((TextField) event.getSource()).setStyle("");
                    setStyle(" -fx-faint-focus-color: rgba(0, 255, 0, 0.8);\n");
                } catch (Exception exp) {
                    ((TextField) event.getSource()).setStyle(" -fx-faint-focus-color: transparent;\n" +
                        "-fx-border-color:rgba(255,0,0,0.9);" +
                        "-fx-background-color: rgba(255,0,0,0.2)");
                }
            };

            left.setOnKeyReleased(e);
            left.setOnKeyReleased(e2->{
                leftValue = Double.parseDouble(left.getText());
            });
            left.setOnMouseClicked(e);
            right.setOnKeyReleased(e);
            right.setOnKeyReleased(e2->{
                rightValue = Double.parseDouble(right.getText());

            });
            right.setOnMouseClicked(e);

            this.getChildren().addAll(new
                    ImageView(t),
                    left,
                    dotcom,
                    right
            );
        }
    }
}
