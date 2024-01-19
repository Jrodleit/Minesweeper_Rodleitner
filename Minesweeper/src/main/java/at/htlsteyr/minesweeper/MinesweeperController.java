package at.htlsteyr.minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MinesweeperController {

    @FXML
    GridPane gridPane = new GridPane();
    private int i = 0;
    private int j = 0;
    private int row = 0;
    private final int MAX = 50;

    int[][] coordinates = new int[MAX][MAX];

    private final Button[][] buttons = new Button[MAX][MAX];

    @FXML
    private void initialize() {
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                buttons[j][k] = new Button();
                buttons[j][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(buttons[j][k], j, k);
                Random random = new Random();
                boolean RandomBoolean = random.nextDouble() < 0.16;

                final int x = j;
                final int y = k;

                buttons[j][k].setOnAction(event -> buttonClick(event, x, y, RandomBoolean));
                buttons[j][k].setOnAction(event -> buttonClick(event,x,y, RandomBoolean));

                if (RandomBoolean){
                    buttons[j][k].setStyle("-fx-background-color: red");
                    buttons[j][k].setOnAction(event -> buttonClick(event,x,y, RandomBoolean));
                    buttons[j][k].setOnAction(event -> Bombs(event, x, y));

                    buttons[j-1][k-1].setStyle("");

                }

            }
        }
    }

    private void buttonClick(ActionEvent event, int x, int y , Boolean random) {
        System.out.println("Coordinates: (" + x + ", " + y + ")");
        System.out.println("Biased random boolean value: " + random);
    }

    private void Bombs(ActionEvent event,int x, int y){
        //System.out.println("Coordinates: (" + x + ", " + y + ")");

        System.out.println("Coordinates: (" + (x-1) + ", " + (y-1) + ")");
        System.out.println("Coordinates: (" + x + ", " + (y-1) + ")");
        System.out.println("Coordinates: (" + (x+1) + ", " + (y-1) + ")");

        System.out.println("\nCoordinates: (" + (x-1) + ", " + y + ")");
        System.out.println("Coordinates: (" + (x+1) + ", " + y + ")");

        System.out.println("\nCoordinates: (" + (x-1) + ", " + (y+1) + ")");
        System.out.println("Coordinates: (" + x + ", " + (y+1) + ")");
        System.out.println("Coordinates: (" + (x+1) + ", " + (y+1) + ")");



    }

}





