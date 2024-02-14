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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Random;

public class MinesweeperController {

    @FXML
    GridPane gridPane = new GridPane();
    @FXML
    ComboBox<String> difficultyComboBox = new ComboBox<>();
    @FXML
    private Label label = new Label();
    Image image = new Image(getClass().getResourceAsStream("flag.png"));
    ImageView imageView = new ImageView(image);

    private int MAXx = 8;
    private int MAXy = 8;
    private Button[][] buttons = new Button[MAXx][MAXy];
    private LinkedList<Bombs> bombList = new LinkedList<Bombs>();
    private boolean[][] visited = new boolean[MAXx][MAXy]; // angeclickte Buttons
    private boolean isFirstClick = true;

    /**
     * Platziert die Buttons auf dem Spielfeld.
     *
     * @param maxX Die maximale Anzahl von Spalten.
     * @param maxY Die maximale Anzahl von Zeilen.
     */
    private void placeButtons(int maxX, int maxY) {
        gridPane.getChildren().clear();
        for (int j = 0; j < maxX; j++) {
            for (int k = 0; k < maxY; k++) {
                buttons[j][k] = new Button();
                buttons[j][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                //buttons[j][k].setMinSize(30, 30);
                buttons[j][k].setMinSize(30, 30);
                buttons[j][k].setMaxSize(30, 30);
                buttons[j][k].setMinWidth(30);
                buttons[j][k].setMaxWidth(30);
                buttons[j][k].setMinHeight(30);
                buttons[j][k].setMaxHeight(30);

                buttons[j][k].setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: transparent; " +
                        "-fx-font-weight: bold; -fx-border-color: #A9A9A9; " + "-fx-border-width: 2px;");

                final int x = j;
                final int y = k;

                buttons[j][k].setOnAction(actionEvent -> buttonClick(actionEvent, x, y));

                buttons[j][k].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if (buttons[x][y].getGraphic() != null && buttons[x][y].getGraphic() instanceof ImageView) {
                            buttons[x][y].setGraphic(null);
                        } else {
                            ImageView flagView = new ImageView(image);
                            buttons[x][y].setGraphic(flagView);
                        }
                    }
                });


                gridPane.add(buttons[j][k], j, k);
            }
        }
    }

    /**
     * Setzt die Schwierigkeit des Spiels.
     */
    private void setDifficulty() {
        difficultyComboBox.setItems(FXCollections.observableArrayList("Einfach", "Mittel", "Schwer"));
        difficultyComboBox.setValue("Einfach");

        // Fügt einen Listener hinzu, der auf Änderungen reagiert
        difficultyComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Einfach":
                    MAXx = 8;
                    MAXy = 8;
                    break;
                case "Mittel":
                    MAXx = 16;
                    MAXy = 16;
                    break;
                case "Schwer":
                    MAXx = 30;
                    MAXy = 16;
                    break;
            }
            resetGame();  // Setzt das Spiel zurück, wenn die Schwierigkeit geändert wird
        });
    }

    /**
     * Initialisiert den Controller.
     */
    @FXML
    private void initialize() {
        setDifficulty();
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        placeButtons(MAXx, MAXy);
    }

    private int firstX = -1;
    private int firstY = -1;

    /**
     * Platziert die Bomben auf dem Spielfeld.
     */
    private void placeBombs() {
        Random random = new Random();
        int bombsToPlace = 10;  // Die Anzahl der Bomben, die platziert werden sollen
        int bombsPlaced = 0;  // Die Anzahl der bereits platzierten Bomben

        if (MAXx == 16) {
            bombsToPlace = 40;
        } else if (MAXx == 30) {
            bombsToPlace = 99;
        }

        // Platziert Bomben, bis die gewünschte Anzahl erreicht ist
        while (bombsPlaced < bombsToPlace) {
            int j = random.nextInt(MAXx);
            int k = random.nextInt(MAXy);

            // Überprüft, ob das ausgewählte Feld oder seine Nachbarn bereits eine Bombe enthalten
            // Wenn nicht, wird eine Bombe platziert
            if (!isNeighbor(j, k, firstX, firstY) && !bombList.contains(new Bombs(j, k))) {
                buttons[j][k].setStyle("-fx-background-color: red");
                buttons[j][k].setOnAction(event -> Bombs(event, j, k));
                bombList.add(new Bombs(j, k));
                bombsPlaced++;
            }
        }
    }

    /**
     * Überprüft, ob zwei Felder Nachbarn sind.
     *
     * @param x1 Die x-Koordinate des ersten Feldes.
     * @param y1 Die y-Koordinate des ersten Feldes.
     * @param x2 Die x-Koordinate des zweiten Feldes.
     * @param y2 Die y-Koordinate des zweiten Feldes.
     * @return true, wenn die Felder Nachbarn sind, ansonsten false.
     */
    private boolean isNeighbor(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1;
    }

    /**
     * Reaktion auf einen Button-Klick.
     *
     * @param event Das ActionEvent-Objekt.
     * @param x     Die x-Koordinate des geklickten Buttons.
     * @param y     Die y-Koordinate des geklickten Buttons.
     */
    private void buttonClick(ActionEvent event, int x, int y) {
        if (isFirstClick) {
            isFirstClick = false;
            firstX = x; // speichern der ersten koordinaten
            firstY = y;
            placeBombs();
            setNumbers();
        }
        int count = countBombs(x, y);
        System.out.println(count);

        buttons[x][y].setStyle("-fx-text-fill: black");
        openButtons(x, y);
        buttons[x][y].setUserData(new Object());
        checkWin();
    }

    /**
     * Setzt das Spiel zurück.
     */
    private void resetGame() {
        bombList.clear();
        visited = new boolean[MAXx][MAXy];
        buttons = new Button[MAXx][MAXy];  // Initialisiert das Button-Array neu

        for (int i = 0; i < MAXx; i++) {
            for (int j = 0; j < MAXy; j++) {
                buttons[i][j] = new Button();  // Erstellt eine neue Button-Instanz
                buttons[i][j].setText("");
                buttons[i][j].setStyle("-fx-text-fill: transparent");
                buttons[i][j].setDisable(false);
            }
        }

        placeButtons(MAXx, MAXy);

        label.setText("");

        isFirstClick = true;
    }

    /**
     * Reaktion auf das Anklicken einer Bombe.
     *
     * @param event Das ActionEvent-Objekt.
     * @param x     Die x-Koordinate der Bombe.
     * @param y     Die y-Koordinate der Bombe.
     */
    private void Bombs(ActionEvent event, int x, int y) {
        label.setStyle("-fx-text-fill: red; -fx-font-scale: 40");
        label.setText("You Lost!");

        for (Bombs bomb : bombList) {
            buttons[bomb.getX()][bomb.getY()].setStyle("-fx-background-color: red");
        }

        for (int i = 0; i < MAXx; i++) {
            for (int j = 0; j < MAXy; j++) {
                buttons[i][j].setDisable(true);
            }
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> resetGame()));
        timeline.play();
    }

    /**
     * Setzt die Zahlen um die Bomben herum.
     */
    private void setNumbers() {
        for (Bombs bomb : bombList) {
            int bombX = bomb.getX();
            int bombY = bomb.getY();

            // Iterate over adjacent cells
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int adjX = bombX + dx;
                    int adjY = bombY + dy;

                    if (isValidCell(adjX, adjY) && !isBomb(adjX, adjY)) {
                        int count = countBombs(adjX, adjY);
                        if (count > 0) {
                            buttons[adjX][adjY].setText(String.valueOf(count));
                        }
                    }
                }
            }
        }
    }

    /**
     * Überprüft, ob eine Zelle gültig ist.
     *
     * @param x Die x-Koordinate der Zelle.
     * @param y Die y-Koordinate der Zelle.
     * @return true, wenn die Zelle gültig ist, ansonsten false.
     */
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < MAXx && y >= 0 && y < MAXy;
    }

    /**
     * Überprüft, ob eine Zelle eine Bombe enthält.
     *
     * @param x Die x-Koordinate der Zelle.
     * @param y Die y-Koordinate der Zelle.
     * @return true, wenn die Zelle eine Bombe enthält, ansonsten false.
     */
    private boolean isBomb(int x, int y) {
        return bombList.stream().anyMatch(bomb -> bomb.getX() == x && bomb.getY() == y);
    }

    /**
     * Zählt die Bomben in den benachbarten Zellen.
     *
     * @param x Die x-Koordinate der Zelle.
     * @param y Die y-Koordinate der Zelle.
     * @return Die Anzahl der Bomben in den benachbarten Zellen.
     */
    private int countBombs(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int adjX = x + dx;
                int adjY = y + dy;
                if (isValidCell(adjX, adjY) && isBomb(adjX, adjY)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Öffnet die benachbarten Zellen.
     *
     * @param x Die x-Koordinate der Zelle.
     * @param y Die y-Koordinate der Zelle.
     */
    private void openButtons(int x, int y) {
        if (x >= 0 && x < MAXx && y >= 0 && y < MAXy && !visited[x][y]) {
            visited[x][y] = true;
            buttons[x][y].setUserData(new Object());

            if (buttons[x][y].getText().isEmpty()) {
                buttons[x][y].setStyle("-fx-text-fill: black");

                // checken der Nachtbarfelder
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newX = x + i;
                        int newY = y + j;
                        if (newX >= 0 && newX < MAXx && newY >= 0 && newY < MAXy && (i != 0 || j != 0)) {
                            openButtons(newX, newY); // Rekursion
                        }
                    }
                }
            } else if (!buttons[x][y].getText().equals("Flagge")) {
                buttons[x][y].setStyle("-fx-text-fill: black");
            }
        }
    }

    /**
     * Überprüft, ob das Spiel gewonnen wurde, indem überprüft wird, ob alle nicht bombenhaltigen
     * Felder geöffnet wurden.
     */
    private void checkWin() {
        for (int i = 0; i < MAXx; i++) {
            for (int j = 0; j < MAXy; j++) {
                // If there's a cell that's not a bomb and not disabled, the game is not won yet
                if (!isBomb(i, j) && buttons[i][j].getUserData() == null) {
                    return;
                }
            }
        }
        // If all non-bomb cells are disabled, the player wins
        winGame();
    }

    /**
     * Zeigt eine Gewinnmeldung an und sperrt alle Buttons, um das Spiel zu beenden.
     * Startet dann einen Timer, um das Spiel zurückzusetzen.
     */
    private void winGame() {
        label.setStyle("-fx-text-fill: green; -fx-font-scale: 40");
        label.setText("You Won!");

        for (int i = 0; i < MAXx; i++) {
            for (int j = 0; j < MAXy; j++) {
                buttons[i][j].setDisable(true);
            }
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> resetGame()));
        timeline.play();
    }
}
