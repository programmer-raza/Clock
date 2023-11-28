
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.text.Text;
import javafx.util.Duration;

public class Clock extends Application {

    Label lblCurrentTime;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a clock and a label
        ClockPane clock = new ClockPane();

        lblCurrentTime = new Label(clock.getHour() + ":" + clock.getMinute()
                + ":" + clock.getSecond());

        // Create a handler for animation
        EventHandler<ActionEvent> eventHandler = e -> {
            clock.setCurrentTime(); // Set a new clock time
            lblCurrentTime.setText(clock.getHour() + ":" + clock.getMinute()
                    + ":" + clock.getSecond());

        };

        // Create an animation for a running clock
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
        // Place clock and label in border pane
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play(); // Start animation
        // Place clock and label in border pane
        BorderPane pane = new BorderPane();
        pane.setCenter(clock);
        pane.setBottom(lblCurrentTime);
        BorderPane.setAlignment(lblCurrentTime, Pos.TOP_CENTER);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 250, 250);
        primaryStage.setTitle("DisplayClock"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

class ClockPane extends Pane {

    // Construct a calendar for the current date and time
    private Calendar calendar = new GregorianCalendar();
    private int hour;
    private int minute;
    private int second;
    double clockRadius;
    double sLength;
    double secondX;
    double secondY;

    // Clock pane's width and height
    private double w = 250, h = 250;
    double centerX;
    double centerY;
    Circle circle;
    Line sLine;
    Line mLine;
    Line hLine;

    double xMinute;
    double mLength;
    double minuteY;
    double hLength;
    double hourX;
    double hourY;

    /**
     * Construct a default clock with the current time
     */
    public ClockPane() {

        // Set current hour, minute and second
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);

        centerX = w / 2;
        centerY = h / 2;
        clockRadius = Math.min(w, h) * 0.8 * 0.5;
        circle = new Circle(centerX, centerY, clockRadius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);

// Draw second hand
        sLength = clockRadius * 0.8;
        secondX = centerX + sLength
                * Math.sin(second * (2 * Math.PI / 60));
        secondY = centerY - sLength
                * Math.cos(second * (2 * Math.PI / 60));
        sLine = new Line(centerX, centerY, secondX, secondY);

        sLine.setStroke(Color.RED);

        // Draw minute hand
        mLength = clockRadius * 0.65;
        xMinute = centerX + mLength
                * Math.sin(minute * (2 * Math.PI / 60));
        minuteY = centerY - mLength
                * Math.cos(minute * (2 * Math.PI / 60));
        mLine = new Line(centerX, centerY, xMinute, minuteY);

        mLine.setStroke(Color.BLUE);

        // Draw hour hand
        hLength = clockRadius * 0.5;
        hourX = centerX + hLength
                * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
        hourY = centerY - hLength
                * Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
        hLine = new Line(centerX, centerY, hourX, hourY);

        hLine.setStroke(Color.GREEN);

        // Draw lines around the clock
        drawLinesAroundClock(centerX, centerY, clockRadius, 6, 5, 8);

        getChildren().addAll(circle, sLine, mLine, hLine);
    }

    /**
     * // * Construct a clock with specified hour, minute, and second //
     */
    public ClockPane(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        paintClock();
    }

    /**
     * Return hour
     */
    public int getHour() {
        int hour12 = hour % 12;
        return (hour12 == 0) ? 12 : hour12;
    }

    /**
     * Set a new hour
     */
    public void setHour(int hour) {
        this.hour = hour;
        paintClock();
    }

    /**
     * Return minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Set a new minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
        paintClock();
    }

    /**
     * Return second
     */
    public int getSecond() {
        return second;
    }

    /**
     * Set a new second
     */
    public void setSecond(int second) {
        this.second = second;
        paintClock();
    }

    /**
     * Return clock pane's width
     */
    public double getW() {
        return w;
    }

    /**
     * Set clock pane's width
     */
    public void setW(double w) {
        this.w = w;
        paintClock();
    }

    /**
     * Return clock pane's height
     */
    public double getH() {
        return h;
    }

    /**
     * Set clock pane's height
     */
    public void setH(double h) {
        this.h = h;
        paintClock();
    }

    /* Set the current time for the clock */
    public void setCurrentTime() {
        calendar = new GregorianCalendar();
        // Set current hour, minute and second
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);

        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);

        paintClock(); // Repaint the clock
    }

    /**
     * Paint the clock
     */
    protected void paintClock() {

        // Calculate the angle between each hour mark
        double angleStep = 360.0 / 12.0;

        // Start angle for positioning the numbers
        double startAngle = 90; // Start at the top (12 o'clock)
        double myradius = clockRadius - 15;
        for (int hour = 12; hour >= 1; hour--) {
            // Calculate the angle in radians
            double radianAngle = Math.toRadians(startAngle);

            // Calculate (x, y) coordinates for each hour label
            double x = centerX + myradius * Math.cos(radianAngle);
            double y = centerY - myradius * Math.sin(radianAngle);

            // Create a Text label and set its position
            Text text = new Text(String.valueOf(hour));
            text.setX(x - 5); // Offset for better centering
            text.setY(y + 5); // Offset for better centering

            getChildren().add(text);

            // Increment the angle for the next hour mark
            startAngle += angleStep;
        }

        //set seconds
        secondX = centerX + sLength
                * Math.sin(second * (2 * Math.PI / 60));
        secondY = centerY - sLength
                * Math.cos(second * (2 * Math.PI / 60));

        sLine.setEndX(secondX);
        sLine.setEndY(secondY);

        //set minute
        xMinute = centerX + mLength
                * Math.sin(minute * (2 * Math.PI / 60));
        minuteY = centerY - mLength
                * Math.cos(minute * (2 * Math.PI / 60));
        mLine.setEndX(xMinute);
        mLine.setEndY(minuteY);

        //set Hour
        hourX = centerX + hLength
                * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
        hourY = centerY - hLength
                * Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));

        hLine.setEndX(hourX);
        hLine.setEndY(hourY);

    }

    private void drawLinesAroundClock(double centerX, double centerY, double clockRadius, double angleStep, double defaultLineLength, double specialLineLength) {
        List<Line> lines = new ArrayList<>();
        for (double angle = 0; angle < 360; angle += angleStep) {
            double startX = centerX + clockRadius * Math.cos(Math.toRadians(angle));
            double startY = centerY - clockRadius * Math.sin(Math.toRadians(angle));

            double endX, endY;

            if (angle % 90 == 0) {
                // At 0, 90, 180, and 270 degrees, use the special line length
                endX = startX - specialLineLength * Math.cos(Math.toRadians(angle));
                endY = startY + specialLineLength * Math.sin(Math.toRadians(angle));
            } else {
                // For other angles, use the default line length
                endX = startX - defaultLineLength * Math.cos(Math.toRadians(angle));
                endY = startY + defaultLineLength * Math.sin(Math.toRadians(angle));
            }

            Line line = new Line(startX, startY, endX, endY);
            lines.add(line);
        }

        getChildren().addAll(lines);
    }

}
