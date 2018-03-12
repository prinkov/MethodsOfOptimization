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
                java.awt.Color.BLACK, java.awt.Color.WHITE), null));

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
                            timeLbl.setText(time[0] + ":" + time[1] + ":" + time[2] + "");
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

        TextField maxTempTF = new TextField("");
        TextField countCycleTF = new TextField("");
        TextField tempDecTF = new TextField("");
        TextField epsTF = new TextField("");


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

        annealingBox.setAlignment(Pos.BOTTOM_CENTER);
        annealingBox.setSpacing(5);
        annealingBox.getChildren().addAll(firstLine, secondLine,
                new Button("Посчитать"), new TextArea());

        startMC.setOnMouseClicked(e-> {
            StringBuffer str = new StringBuffer();
            startMC.setDisable(true);
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
                                    time[1] + " сек., " + time[2] + " мс. \n");

                            return null;
                        }
                    };

                }
            };
            service.start();
            service.setOnSucceeded(ee-> {
                startMC.setDisable(false);
                outputMC.setText(str.toString());
            });

        });


        Tab tabMC = new Tab();
        Tab tabAS =new Tab();

        tabMC.setContent(monteCarloBox);
        tabMC.setText("Метод монте карло");
        tabAS.setContent(annealingBox);
        tabAS.setText("Метод иммитация отжига");
        tabMC.setClosable(false);
        tabAS.setClosable(false);
        tabPane.getTabs().addAll(tabMC, tabAS);

        HBox timeBox = new HBox();
        timeBox.setSpacing(5);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.getChildren().addAll(new Label("Время выполнения: "), timeLbl);

        vboxMain.getChildren().addAll(varSc, tabPane, new Separator(),  timeBox);
        tabPane.setTabMinWidth(250);


        Scene scene = new Scene(vboxMain);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            tabPane.setTabMinWidth(newValue.floatValue() / 2.0 - 30);
            tabPane.setTabMaxWidth(newValue.floatValue() / 2.0 - 30);
//            pbMC.setMinWidth(newValue.floatValue() - 50);
        };

        stage.widthProperty().addListener(stageSizeListener);

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
                    java.awt.Color.BLACK, java.awt.Color.WHITE), null));
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
