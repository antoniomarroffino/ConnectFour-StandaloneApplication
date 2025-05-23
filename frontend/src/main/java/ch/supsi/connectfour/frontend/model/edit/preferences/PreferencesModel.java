package ch.supsi.connectfour.frontend.model.edit.preferences;

import ch.supsi.connectfour.backend.application.GridController;
import ch.supsi.connectfour.backend.application.GridControllerInterface;
import ch.supsi.connectfour.backend.application.exceptions.IllegalPreferencesException;
import ch.supsi.connectfour.backend.application.preferences.PreferencesController;
import ch.supsi.connectfour.backend.application.preferences.PreferencesControllerInterface;
import ch.supsi.connectfour.backend.business.domain.Piece;
import ch.supsi.connectfour.backend.business.domain.Player;
import ch.supsi.connectfour.frontend.controller.edit.preferences.PreferencesModelInterface;

import java.util.List;
import java.util.Set;

public class PreferencesModel implements PreferencesModelInterface {
    private static PreferencesModel instance = null;
    private final PreferencesControllerInterface preferencesController;
    private final GridControllerInterface gridController;

    private PreferencesModel() {
        this.preferencesController = PreferencesController.getInstance();
        this.gridController = GridController.getInstance();
    }

    public static PreferencesModel getInstance() {
        return instance == null ? instance = new PreferencesModel() : instance;
    }

    @Override
    public Set<String> getSupportedColors() {
        return preferencesController.getSupportedColors();
    }

    @Override
    public Set<String> getSupportedSymbols() {
        return preferencesController.getSupportedSymbols();
    }

    @Override
    public List<Player> getPlayers() {
        return gridController.getPlayers();
    }

    @Override
    public void setNewPreferences(List<Piece> pieces) throws IllegalPreferencesException {
        preferencesController.setNewPreferences(pieces);
    }
}
