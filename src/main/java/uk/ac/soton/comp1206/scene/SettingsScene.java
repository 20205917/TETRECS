package uk.ac.soton.comp1206.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.SliderBox;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utils.Multimedia;

import java.io.*;


/**
 * The Settings Scene is used for displaying the settings of the game
 */
public class SettingsScene extends BaseScene {
    private static final Logger logger = LogManager.getLogger(SettingsScene.class);

    //Volume configuration
    public static double musicVolume = 50, audioVolume = 50;

    public static int speedLevel = 1, healthPoint = 1;

    public static Text theme = new Text("challenge-background1");

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     */
    public SettingsScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Settings Scene");
    }


    /**
     * load settings from settings.txt
     */
    public static void loadSettings() {
        if (new File("settings.txt").exists()) {
            try {
                FileInputStream reader = new FileInputStream("settings.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(reader));
                try {
                    //read 4 lines from settings.txt
                    musicVolume = Double.parseDouble(bufferedReader.readLine());
                    Multimedia.setMusicVolume(musicVolume);
                    audioVolume = Double.parseDouble(bufferedReader.readLine());
                    Multimedia.setAudioVolume(audioVolume);
                    speedLevel = Integer.parseInt(bufferedReader.readLine());
                    healthPoint = Integer.parseInt(bufferedReader.readLine());
                    theme.setText(bufferedReader.readLine());
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.info("settings.txt not found");
            }
        } else {
            writeSettings();
        }
    }

    /**
     * Write settings to settings.txt
     */
    public static void writeSettings() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("settings.txt"));
            bufferedWriter.write(musicVolume + "\n");
            bufferedWriter.write(audioVolume + "\n");
            bufferedWriter.write(speedLevel + "\n");
            bufferedWriter.write(healthPoint + "\n");
            bufferedWriter.write(theme.getText() + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Initialise the Settings Scene
     */
    @Override
    public void initialise() {
        logger.info("Initializing " + this.getClass().getName());
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> gameWindow.startMenu();
            }
        });
    }

    /**
     * Build the Settings Scene
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

        var settingsPane = new StackPane();
        settingsPane.setMaxWidth(gameWindow.getWidth());
        settingsPane.setMaxHeight(gameWindow.getHeight());
        settingsPane.getStyleClass().add(SettingsScene.theme.getText());
        root.getChildren().add(settingsPane);

        var mainPane = new BorderPane();
        settingsPane.getChildren().add(mainPane);

        /* Top */
        var topBar = new HBox(200);
        topBar.setAlignment(Pos.CENTER);
        BorderPane.setMargin(topBar, new Insets(10, 0, 0, 0));
        mainPane.setTop(topBar);

        // Title
        var title = new Text("Settings");
        title.getStyleClass().add("title");
        topBar.getChildren().add(title);

        /* Center */
        var centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        mainPane.setCenter(centerBox);

        var volumeBox = new HBox(100);
        volumeBox.setAlignment(Pos.CENTER);
        var volumeControl = new Text("Volume Control");
        volumeControl.getStyleClass().add("heading");

        // Music
        var musicBox = new SliderBox("Music", 10, musicVolume);
        musicBox.getSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            musicVolume = musicBox.getSlider().getValue();
            Multimedia.setMusicVolume(musicVolume / 100);
        });

        // Audio
        var audioBox = new SliderBox("Audio", 10, audioVolume);
        audioBox.getSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            audioVolume = (int) audioBox.getSlider().getValue();
            Multimedia.setAudioVolume(audioVolume / 100);
        });

        volumeBox.getChildren().addAll(musicBox, audioBox);

        // Theme selector
        var themeGrid = new GridPane();
        themeGrid.setAlignment(Pos.CENTER);
        themeGrid.setHgap(10);
        themeGrid.setVgap(10);
        var selectLabel = new Text("Select Theme");
        selectLabel.getStyleClass().add("heading");
        GridPane.setHalignment(selectLabel, HPos.CENTER);
        themeGrid.add(selectLabel, 0, 0, 3, 1);

        ImageView[] allTheme = new ImageView[6];
        for (int i = 0; i <= 5; i++) {
            allTheme[i] = Multimedia.getImageView((i + 1) + ".jpg");
            themeGrid.add(allTheme[i], i % 3, i / 3 + 1);
            allTheme[i].setFitWidth(240);
            allTheme[i].setPreserveRatio(true);
            final int num = i + 1;
            allTheme[i].setOnMouseClicked(e -> {
                Multimedia.playAudio("pling.wav");
                theme = new Text("challenge-background" + num);
                logger.info("Set theme " + num);
            });
        }
        centerBox.getChildren().addAll(volumeControl, volumeBox, themeGrid);

        /* Bottom */
        var bottomBar = new VBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(20);
        BorderPane.setMargin(bottomBar, new Insets(10, 10, 20, 0));
        mainPane.setBottom(bottomBar);

        /*speed level*/
        // Title
        var speed1 = new Text("Speed Level");
        speed1.getStyleClass().add("title");
        ObservableList<String> levelOptions =
                FXCollections.observableArrayList(
                        "Level X1",
                        "Level X2",
                        "Level X3",
                        "Level X4",
                        "Level X5"
                );
        ComboBox<String> speedLevelBox = new ComboBox<>();
        speedLevelBox.setItems(levelOptions);
        speedLevelBox.setValue("Level X" + speedLevel);
        speedLevelBox.setPrefSize(200, 30);
        speedLevelBox.valueProperty().addListener((observable, oldValue, newValue) ->
                speedLevel = Integer.parseInt(speedLevelBox.getValue().substring(speedLevelBox.getValue().length() - 1))
        );
        /*Health Point*/
        var healthy1 = new Text("Health Point");
        healthy1.getStyleClass().add("title");
        ObservableList<String> healthOptions =
                FXCollections.observableArrayList(
                        "Health Point : 1",
                        "Health Point : 2",
                        "Health Point : 3"
                );
        ComboBox<String> HealthPointBox = new ComboBox<>();
        HealthPointBox.setItems(healthOptions);
        HealthPointBox.setValue("Health Point : " + healthPoint);
        HealthPointBox.setPrefSize(200, 30);
        HealthPointBox.valueProperty().addListener((observable, oldValue, newValue) ->
                healthPoint = Integer.parseInt(HealthPointBox.getValue().substring(HealthPointBox.getValue().length() - 1))
        );

        // Save button
        var saveButton = new Button("Save");
        saveButton.getStyleClass().add("channelItem");
        saveButton.setOnMouseClicked(e -> {
            writeSettings();
            gameWindow.startMenu();
        });
        bottomBar.getChildren().addAll(speed1, speedLevelBox, healthy1, HealthPointBox, saveButton);


    }
}