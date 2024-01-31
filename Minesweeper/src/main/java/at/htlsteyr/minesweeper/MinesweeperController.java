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
    private int j = 0;
    private int k = 0;
    private final int MAX = 8;
    private final Button[][] buttons = new Button[MAX][MAX];
    private LinkedList<Bombs> bombList = new LinkedList<Bombs>();
    private ListIterator<Bombs> bombIterator = bombList.listIterator();


    private boolean randomBombs;

    @FXML
    private void initialize() {
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                buttons[j][k] = new Button();
                buttons[j][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(buttons[j][k], j, k);
                Random random = new Random();
                randomBombs = random.nextDouble() < 0.16;

                final int x = j;
                final int y = k;

                buttons[j][k].setOnAction(event -> buttonClick(event, x, y, randomBombs));
                buttons[j][k].setOnAction(event -> buttonClick(event, x, y, randomBombs));

                if (randomBombs) {
                    buttons[j][k].setStyle("-fx-background-color: red");
                    buttons[j][k].setOnAction(event -> Bombs(event, x, y));
                    bombList.add(new Bombs(x, y, true));
                }
            }
        }
        setNumbers();
    }


    private void buttonClick(ActionEvent event, int x, int y, Boolean random) {
        System.out.println("Coordinates: (" + x + ", " + y + ")");
        System.out.println("Random value: " + random);
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
            int surroundingBombs = 1;

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
        for (Bombs bomb : bombList){
            buttons[bomb.getX()][bomb.getY()].setText("");
        }

    }
}












