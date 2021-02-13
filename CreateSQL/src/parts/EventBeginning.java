package parts;

public class EventBeginning {

	private ListenerInterface listener = null;

	public void eventListener() {

		if(listener != null) {
			listener.buttonAction();
		}
	}

	public void addListener(ListenerInterface listener) {

		this.listener = listener;
	}
}
