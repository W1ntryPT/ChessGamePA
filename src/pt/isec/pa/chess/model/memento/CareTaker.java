package pt.isec.pa.chess.model.memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class CareTaker {
	IOriginator originator;

	Deque<IMemento> undo;
	Deque<IMemento> redo;

	public CareTaker(IOriginator originator) {
		this.originator = originator;
		undo = new ArrayDeque<>();
		redo = new ArrayDeque<>();
	}


	public void save() {
		redo.clear();
		undo.push(originator.save());
	}

	public void undo() {
		if (undo.isEmpty())
			return;
		redo.push(originator.save());
		originator.restore(undo.pop());
	}

	public void redo() {
		if (redo.isEmpty())
			return;
		undo.push(originator.save());
		originator.restore(redo.pop());
	}

	public void reset() {
		undo.clear();
		redo.clear();
	}

	public boolean hasUndo() { return !undo.isEmpty(); }

	public boolean hasRedo() { return !redo.isEmpty(); }

}
