package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ModelLog {
	public static final String LOG = "LOG";

	private static ModelLog _instance = null;
	private static PropertyChangeSupport _pcs = null;

	public static ModelLog getInstance() {
		if (_instance == null) {
			_instance = new ModelLog();
			_pcs = new PropertyChangeSupport(_instance);
		}
		return _instance;
	}

	protected ArrayList<String> log;
	private ModelLog() {
		log = new ArrayList<>();
	}

	public void reset() {
		log.clear();
		_pcs.firePropertyChange(LOG, null, null);
	}

	public void log(String msg) {
		log.add(msg);
		_pcs.firePropertyChange(LOG, null, null);
	}

	public List<String> getLog() {
		return new ArrayList<>(log);
	}


	public void addListener(PropertyChangeListener listener) { _pcs.addPropertyChangeListener(listener); }

	public void addListener(String property, PropertyChangeListener listener) { _pcs.addPropertyChangeListener(property,listener); }

}
