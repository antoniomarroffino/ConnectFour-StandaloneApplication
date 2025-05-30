package ch.supsi.connectfour.frontend;

import ch.supsi.connectfour.frontend.controller.GameController;
import ch.supsi.connectfour.frontend.controller.InfoBarController;
import ch.supsi.connectfour.frontend.controller.InfoBarControllerInterface;
import ch.supsi.connectfour.frontend.controller.column.ColumnController;
import ch.supsi.connectfour.frontend.controller.column.ColumnControllerInterface;
import ch.supsi.connectfour.frontend.controller.column.ColumnViewInterface;
import ch.supsi.connectfour.frontend.controller.edit.language.LanguageController;
import ch.supsi.connectfour.frontend.controller.edit.language.UpdaterLanguageInterface;
import ch.supsi.connectfour.frontend.controller.edit.preferences.PreferencesController;
import ch.supsi.connectfour.frontend.controller.edit.preferences.PreferencesViewInterface;
import ch.supsi.connectfour.frontend.controller.savingGame.SavingGameViewInterface;
import ch.supsi.connectfour.frontend.controller.statusGame.StatusGameController;
import ch.supsi.connectfour.frontend.controller.statusGame.UpdateStatusInterface;
import ch.supsi.connectfour.frontend.dispatcher.GameControllerInterface;
import ch.supsi.connectfour.frontend.dispatcher.StatusGameControllerInterface;
import ch.supsi.connectfour.frontend.dispatcher.edit.language.LanguageControllerInterface;
import ch.supsi.connectfour.frontend.dispatcher.edit.preferences.PreferencesControllerInterface;
import ch.supsi.connectfour.frontend.model.UpdateGridInterface;
import ch.supsi.connectfour.frontend.view.InfoBar;
import ch.supsi.connectfour.frontend.view.about.AboutView;
import ch.supsi.connectfour.frontend.view.column.ColumnView;
import ch.supsi.connectfour.frontend.view.menubar.MenuBarView;
import ch.supsi.connectfour.frontend.view.preferences.PreferencesView;
import ch.supsi.connectfour.frontend.view.prestart.PreStartView;
import ch.supsi.connectfour.frontend.view.prestart.PreStartViewInterface;
import ch.supsi.connectfour.frontend.view.saving.SavingView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainFx extends Application {
    public static final String APP_TITLE = "Connect4";
    public static ResourceBundle resourceBundle;
    public static BorderPane mainBorderPane;
    public static Parent board;
    public static BorderPane gameBoardBorderPane = new BorderPane();

    private final LanguageControllerInterface languageController;
    private final StatusGameControllerInterface statusGameController;
    private final GameControllerInterface gameController;
    private final PreferencesControllerInterface preferencesController;
    private final InfoBarControllerInterface infoBarController;
    private final ColumnControllerInterface columnController;

    private final ColumnViewInterface columnView;
    private final UpdaterLanguageInterface savingGameView;
    private final UpdateStatusInterface preStartView;
    private final UpdaterLanguageInterface menuBarView;
    private final PreferencesViewInterface preferencesView;
    public static UpdateGridInterface boardView;
    private final UpdaterLanguageInterface aboutView;

    public MainFx() {
        this.languageController = LanguageController.getInstance();
        this.gameController = GameController.getInstance();
        this.preferencesController = PreferencesController.getInstance();
        this.infoBarController = InfoBarController.getInstance();
        this.statusGameController = StatusGameController.getInstance();
        this.columnController = ColumnController.getInstance();
        this.columnView = ColumnView.getInstance();
        this.savingGameView = SavingView.getInstance();
        this.preStartView = PreStartView.getInstance();
        this.menuBarView = MenuBarView.getInstance();
        this.preferencesView = PreferencesView.getInstance();
        this.aboutView = AboutView.getInstance();
        resourceBundle = ResourceBundle.getBundle("i18n.labels");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // handle the main window close request
        // in real life, this event should not be dealt with here!
        // it should actually be delegated to a suitable ExitController!
        primaryStage.setOnCloseRequest(
                windowEvent -> {
                    // consume the window event (the main window would be closed otherwise no matter what)
                    windowEvent.consume();

                    // hard close the primary stage
                    // javafx guarantees the clean exit of the javafx platform, when the last application stage is closed
                    primaryStage.close();
                }
        );

        //PREFERENCES DISPATCHER
        try {
            URL fxmlPreferencesDispatcher = getClass().getResource("/preferences.fxml");
            if (fxmlPreferencesDispatcher == null) {
                return;
            }
            FXMLLoader preferencesLoader = new FXMLLoader(fxmlPreferencesDispatcher, resourceBundle);
            preferencesLoader.load();
            this.preferencesController.addPreferencesView(preferencesView);
            this.languageController.addUpdaterLanguageList((UpdaterLanguageInterface) preferencesView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //PRESTART DISPATCHER
        statusGameController.addUpdateViewByStatus(preStartView);

        // ABOUT
        this.languageController.addUpdaterLanguageList(aboutView);

        // SAVEPOPUPCHOICE
        try {
            URL fxmlSaveChoiceUrl = getClass().getResource("/saveGamePopUp.fxml");
            if (fxmlSaveChoiceUrl == null) {
                return;
            }
            FXMLLoader saveChoiceLoader = new FXMLLoader(fxmlSaveChoiceUrl, resourceBundle);
            saveChoiceLoader.load();
            this.languageController.addUpdaterLanguageList(savingGameView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // MENU BAR
        MenuBar menuBar;
        try {
            URL fxmlUrl = getClass().getResource("/menubar.fxml");
            if (fxmlUrl == null) {
                return;
            }
            FXMLLoader menuBarLoader = new FXMLLoader(fxmlUrl, resourceBundle);
            menuBar = menuBarLoader.load();

            UpdateStatusInterface menuBarDispatcher = menuBarLoader.getController();
            this.languageController.addUpdaterLanguageList(menuBarView);
            this.statusGameController.addUpdateViewByStatus(menuBarDispatcher);
            this.statusGameController.addUpdateViewByStatus((UpdateStatusInterface) menuBarView);
            ((MenuBarView) menuBarView).setContainerMenuBar(menuBar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // CONNECT-FOUR COLUMN SELECTORS
        Parent columnSelectors;
        try {
            URL fxmlUrl = getClass().getResource("/columnselectors.fxml");
            if (fxmlUrl == null) {
                return;
            }

            FXMLLoader columnSelectorsLoader = new FXMLLoader(fxmlUrl, resourceBundle);
            columnSelectors = columnSelectorsLoader.load();
            this.columnController.addColumnView(columnView);
            this.columnView.addColumnButtonGridPane((GridPane) columnSelectors);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // CONNECT-FOUR BOARD
        try {
            URL fxmlUrl = getClass().getResource("/gameboard.fxml");
            if (fxmlUrl == null) {
                return;
            }

            FXMLLoader boardLoader = new FXMLLoader(fxmlUrl, resourceBundle);
            board = boardLoader.load();
            boardView = boardLoader.getController();
            this.gameController.addUpdaterGrid(boardView);
            this.preferencesController.addUpdaterGrid(boardView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // INFO BAR
        Parent infoBar;
        try {
            URL fxmlUrl = getClass().getResource("/infobar.fxml");
            if (fxmlUrl == null) {
                // resource file not found
                return;
            }

            FXMLLoader infoBarLoader = new FXMLLoader(fxmlUrl, resourceBundle);
            infoBar = infoBarLoader.load();
            UpdaterLanguageInterface infoBarView = infoBarLoader.getController();
            this.languageController.addUpdaterLanguageList(infoBarView);
            this.infoBarController.addInfoBar((InfoBar) infoBarView);
            this.statusGameController.addUpdateViewByStatus((UpdateStatusInterface) infoBarView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // BORDER PANE
        mainBorderPane = new BorderPane();


        mainBorderPane.setTop(menuBar);
        gameBoardBorderPane.setTop(columnSelectors);
        gameBoardBorderPane.setCenter(board);

        ((PreStartViewInterface) preStartView).addMainBorderPain(mainBorderPane);
        ((PreStartViewInterface) preStartView).addGameBorderPain(gameBoardBorderPane);
        statusGameController.setStatusToPreStart();

        mainBorderPane.setBottom(infoBar);

        // SCENE
        Scene scene = new Scene(mainBorderPane);

        // PRIMARY STAGE
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/connect-four.png"))));
        primaryStage.setTitle(MainFx.APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        ((SavingGameViewInterface) savingGameView).addMainBorderPain(mainBorderPane);
    }
}
