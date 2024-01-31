package at.htlsteyr.minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class MinesweeperController {

    @FXML
    GridPane gridPane = new GridPane();
    private final int j = 0;
    private final int k = 0;
    private final int MAX = 8;
    private final Button[][] buttons = new Button[MAX][MAX];
    private LinkedList<Bombs> bombList = new LinkedList<Bombs>();
    private ListIterator<Bombs> bombIterator = bombList.listIterator();

    @FXML
    private void initialize() {
        //Setzen der Buttons
        for (int j = 0; j < MAX; j++) {
            for (int k = 0; k < MAX; k++) {
                buttons[j][k] = new Button();
                buttons[j][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(buttons[j][k], j, k);

                final int x = j;
                final int y = k;

                buttons[j][k].setOnAction(actionEvent -> buttonClick(actionEvent, y, x));
            }
        }

        //Setzen von Bomben
        Random random = new Random();
        int bombsToPlace = 10;
        int bombsPlaced = 0;

        while (bombsPlaced <= bombsToPlace) {
            int j = random.nextInt(MAX);
            int k = random.nextInt(MAX);

            if (!bombList.contains(new Bombs(j, k, false))) {
                buttons[j][k].setStyle("-fx-background-color: red");
                buttons[j][k].setOnAction(event -> Bombs(event, j, k));
                bombList.add(new Bombs(j, k, true));
                bombsPlaced++;
            }
        }
        setNumbers();
    }


    private void buttonClick(ActionEvent event, int x, int y) {
        System.out.println("Coordinates: (" + x + ", " + y + ")");
    }

    private void Bombs(ActionEvent event, int x, int y) {

        System.out.println("You lost");

        System.out.println("Coordinates: (" + (x - 1) + ", " + (y - 1) + ")");
        System.out.println("Coordinates: (" + x + ", " + (y - 1) + ")");
        System.out.println("Coordinates: (" + (x + 1) + ", " + (y - 1) + ")");

        System.out.println("\nCoordinates: (" + (x - 1) + ", " + y + ")");
        System.out.println("Coordinates: (" + (x + 1) + ", " + y + ")");

        System.out.println("\nCoordinates: (" + (x - 1) + ", " + (y + 1) + ")");
        System.out.println("Coordinates: (" + x + ", " + (y + 1) + ")");
        System.out.println("Coordinates: (" + (x + 1) + ", " + (y + 1) + ")");

    }

    private void setNumbers() {

        for (Bombs bomb : bombList) {
            int bombX = bomb.getX();
            int bombY = bomb.getY();

            for (int i = 0; i < 3; i++) {
                for (int l = 0; l < 3; l++) {
                    // Nummern setzen der linken Spalte
                    if (bombX > 0) {
                        buttons[bombX - 1][bombY].setText("1");
                        if (bombY > 0) {
                            buttons[bombX - 1][bombY - 1].setText("1");
                        }
                        if (bombY < 7) {
                            buttons[bombX - 1][bombY + 1].setText("1");
                        }
                    }

                    // Nummern setzen der mittleren Spalte
                    if (bombY > 0) {
                        buttons[bombX][bombY - 1].setText("1");
                    }
                    if (bombY < 7) {
                        buttons[bombX][bombY + 1].setText("1");
                    }

                    // Nummern setzen der rechten Spalte
                    if (bombX < 7) {
                        buttons[bombX + 1][bombY].setText("1");
                        if (bombY > 0) {
                            buttons[bombX + 1][bombY - 1].setText("1");
                        }
                        if (bombY < 7) {
                            buttons[bombX + 1][bombY + 1].setText("1");
                        }
                    }
                }
            }
        }
        for (Bombs bomb : bombList) {
            buttons[bomb.getX()][bomb.getY()].setText("");
        }
    }
}