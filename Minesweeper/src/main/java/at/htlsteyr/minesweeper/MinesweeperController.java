/*-----------------------------------------------------------------------------
 *              Hoehere Technische Bundeslehranstalt STEYR
 *           Fachrichtung Elektronik und Technische Informatik
 *----------------------------------------------------------------------------*/
/**
 * Kurzbeschreibung
 *
 * @author  : Joschua Rodleitner
 * @date    : 07.02.2024
 *
 * @details
 *   Dieser Controller implementiert die Logik des Minesweeper-Spiels. Es bietet Funktionen zum Platzieren von Buttons
 *   , zum Setzen von Bomben, zum Anzeigen von Zahlen um die Bomben herum und zum Öffnen von leeren Bereichen.
 *   Das Spiel beginnt, sobald der Benutzer den ersten Button klickt. Wenn eine Bombe angeklickt wird,
 *   wird das Spiel beendet und eine entsprechende Meldung ausgegeben. Der Benutzer kann Flaggen platzieren und entfernen,
 *   um potenzielle Bomben zu markieren. Das Öffnen von leeren Bereichen erfolgt rekursiv, um benachbarte leere Felder
 *   automatisch zu öffnen. Die Anzahl der Bomben um ein Feld herum wird angezeigt, um dem Spieler Hinweise zu geben,
 *   wo sich Bomben befinden könnten.
 *
 */

/**
 * \addtogroup modulname
 * @{
 */

package at.htlsteyr.minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;


public class MinesweeperController {

    @FXML
    GridPane gridPane = new GridPane();
    @FXML
    ComboBox<String> difficultyComboBox = new ComboBox<>();
    @FXML
    private Label label = new Label();
    private int MAX = 8;
    private final Button[][] buttons = new Button[MAX][MAX];
    private LinkedList<Bombs> bombList = new LinkedList<Bombs>();
    private boolean[][] visited = new boolean[MAX][MAX]; // angeclickte Buttons
    private boolean isFirstClick = true;

    private void placeButtons(int max) {
        for (int j = 0; j < max; j++) {
            for (int k = 0; k < max; k++) {
                buttons[j][k] = new Button();
                buttons[j][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                buttons[j][k].setMinSize(30, 30);
                gridPane.add(buttons[j][k], j, k);
                buttons[j][k].setStyle("-fx-background-radius: 0; -fx-text-fill: transparent ");//-fx-text-fill: transparent`

                final int x = j;
                final int y = k;

                buttons[j][k].setOnAction(actionEvent -> buttonClick(actionEvent, x, y));

                buttons[j][k].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    String tempStyle = buttons[x][y].getStyle();
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if (buttons[x][y].getText().equals("Flagge")) {
                            if (tempStyle.equals("-fx-text-fill: transparent")) {
                                buttons[x][y].setText("");
                                setNumbers();
                                buttons[x][y].setStyle("-fx-text-fill: transparent");
                            } else {
                                buttons[x][y].setText("");
                                setNumbers();
                                buttons[x][y].setStyle("-fx-text-fill: black");
                            }
                        } else {
                            buttons[x][y].setText("Flagge");
                            buttons[x][y].setStyle("-fx-text-fill: black");
                        }
                    }
                });
            }
        }
    }

    private void setDifficulty() {
        difficultyComboBox.setItems(FXCollections.observableArrayList("Einfach", "Mittel", "Schwer"));
        difficultyComboBox.setValue("Einfach");
        if (difficultyComboBox.getValue().equals("Einfach")){
            placeButtons(MAX);
        }
        if (difficultyComboBox.getValue().equals("Mittel")){
            MAX=16;
            placeButtons(MAX);
        }
        if (difficultyComboBox.getValue().equals("Schwer")){
            placeButtons(MAX);
        }
    }

    @FXML
    private void initialize() {
        setDifficulty();
    }

    private int firstX = -1;
    private int firstY = -1;

    private void placeBombs() {
        //Setzen von Bomben
        Random random = new Random();
        int bombsToPlace = 10;
        int bombsPlaced = 0;

        while (bombsPlaced <= bombsToPlace) {
            int j = random.nextInt(MAX);
            int k = random.nextInt(MAX);

            if (!bombList.contains(new Bombs(j, k)) && (j != firstX || k != firstY)) {
                // keine Bomben setzten auf den ersten Button
                //buttons[j][k].setStyle("-fx-background-color: red");
                buttons[j][k].setOnAction(event -> Bombs(event, j, k));
                bombList.add(new Bombs(j, k));
                bombsPlaced++;
            }
        }
        setNumbers();
    }

    private void buttonClick(ActionEvent event, int x, int y) {
        if (isFirstClick) {
            isFirstClick = false;
            firstX = x; // Speichern der Koordinaten des ersten Buttons
            firstY = y;
            placeBombs();
        }
        int count = countBombs(x,y);
        System.out.println(count);

        buttons[x][y].setStyle("-fx-text-fill: black");
        openButtons(x, y);
    }


    private void resetGame() {
        bombList.clear();
        visited = new boolean[MAX][MAX];

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setStyle("-fx-text-fill: transparent");
                buttons[i][j].setDisable(false);
            }
        }

        label.setText("");

        isFirstClick = true;

        if (!isFirstClick) {
            placeBombs();
        }
    }

    private void Bombs(ActionEvent event, int x, int y) {
        label.setStyle("-fx-text-fill: red; -fx-font-scale: 20");
        label.setText("You Lost!");

        for (Bombs bomb : bombList) {
            buttons[bomb.getY()][bomb.getX()].setStyle("-fx-background-color: red");
        }

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                buttons[i][j].setDisable(true);
            }
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> resetGame()));
        timeline.play();
    }

    private void setNumbers() {
        for (Bombs bomb : bombList) {
            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int count;

            // Nummern setzen der linken Spalte
            if (bombX > 0) {
                count = countBombs(bombX - 1, bombY);
                if (count > 0) {
                    buttons[bombX - 1][bombY].setText(String.valueOf(count));
                }
                if (bombY > 0) {
                    count = countBombs(bombX - 1, bombY - 1);
                    if (count > 0) {
                        buttons[bombX - 1][bombY - 1].setText(String.valueOf(count));
                    }
                }
                if (bombY < 7) {
                    count = countBombs(bombX - 1, bombY + 1);
                    if (count > 0) {
                        buttons[bombX - 1][bombY + 1].setText(String.valueOf(count));
                    }
                }
            }
            // Nummern setzen der mittleren Spalte
            if (bombY > 0) {
                count = countBombs(bombX, bombY - 1);
                if (count > 0) {
                    buttons[bombX][bombY - 1].setText(String.valueOf(count));
                }
            }
            if (bombY < 7) {
                count = countBombs(bombX, bombY + 1);
                if (count > 0) {
                    buttons[bombX][bombY + 1].setText(String.valueOf(count));
                }
            }
            // Nummern setzen der rechten Spalte
            if (bombX < 7) {
                count = countBombs(bombX + 1, bombY);
                if (count > 0) {
                    buttons[bombX + 1][bombY].setText(String.valueOf(count));
                }
                if (bombY > 0) {
                    count = countBombs(bombX + 1, bombY - 1);
                    if (count > 0) {
                        buttons[bombX + 1][bombY - 1].setText(String.valueOf(count));
                    }
                }
                if (bombY < 7) {
                    count = countBombs(bombX + 1, bombY + 1);
                    if (count > 0) {
                        buttons[bombX + 1][bombY + 1].setText(String.valueOf(count));
                    }
                }
            }
        }
        for (Bombs bomb : bombList) {
            buttons[bomb.getX()][bomb.getY()].setText("");
        }
    }

    private int countBombs(int x, int y) {
        int count = 0;
        for (Bombs bomb : bombList) {
            int bombX = bomb.getX();
            int bombY = bomb.getY();
            // check ob eine bombe im 3x3 feld ist
            if (Math.abs(bombX - x) <= 1 && Math.abs(bombY - y) <= 1) {
                if (!(bombX == x && bombY == y)) {
                    count++;
                }
            }
        }
        return count;
    }
    private void openButtons(int x, int y) {
        if (x >= 0 && x < MAX && y >= 0 && y < MAX && !visited[x][y]) {
            visited[x][y] = true;

            if (buttons[x][y].getText().isEmpty()) {
                buttons[x][y].setStyle("-fx-text-fill: black");

                // Check the neighboring buttons
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newX = x + i;
                        int newY = y + j;
                        if (newX >= 0 && newX < MAX && newY >= 0 && newY < MAX && (i != 0 || j != 0)) {
                            openButtons(newX, newY); // Rekursion
                        }
                    }
                }
            } else if (!buttons[x][y].getText().equals("Flagge")) {
                buttons[x][y].setStyle("-fx-text-fill: black");
            }
        }
    }
}