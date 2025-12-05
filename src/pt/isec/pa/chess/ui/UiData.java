package pt.isec.pa.chess.ui;

import pt.isec.pa.chess.ui.res.SoundManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UiData {
    private static UiData _instance = null;

    public static final String SETTING_CHANGE = "SETTING";

    private static PropertyChangeSupport _pcs;

    private boolean learningMode;

    private boolean showMoves;

    private boolean sound;
    private String language;

    public static UiData getInstance() {
        if (_instance == null) {
            _instance = new UiData(_pcs);
            _pcs = new PropertyChangeSupport(_instance);
        }
        return _instance;
    }

    private UiData(PropertyChangeSupport pcs){
        this.language = SoundManager.EN;
        this.sound = false;
        this.learningMode = false;
        this.showMoves = false;
    }


    public boolean isSoundOn() { return this.sound; }

    public void setSound(boolean isOn){
        this.sound = isOn;
        _pcs.firePropertyChange(SETTING_CHANGE,null,null);
    }

    public String getLanguage() { return this.language; }

    public void setLanguage(String language){
        this.language = language;
        _pcs.firePropertyChange(SETTING_CHANGE,null,null);
    }

    public boolean learningMode() { return this.learningMode; }

    public boolean isShowMoves() { return this.showMoves; }


    public void setLearningMode(boolean isOn) {
        this.learningMode = isOn;
        _pcs.firePropertyChange(SETTING_CHANGE,null,null);
    }

    public void setShowMoves(boolean isOn) {
        this.showMoves = isOn;
        _pcs.firePropertyChange(SETTING_CHANGE,null,null);
    }

    public void addListener(String property, PropertyChangeListener listener) { _pcs.addPropertyChangeListener(property,listener); }

}
