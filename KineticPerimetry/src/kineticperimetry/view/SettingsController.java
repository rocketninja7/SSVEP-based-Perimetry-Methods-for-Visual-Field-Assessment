package kineticperimetry.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SettingsController {
	@FXML
	private ComboBox<String> test;
	
	@FXML
	private void initialize() {
		ObservableList<String> options=FXCollections.observableArrayList();
		options.addAll("Kinetic Perimetry", "Static Perimetry");
		test.itemsProperty().setValue(options);
	}
	
	@FXML
	void startPressed() {
		switch((String)test.getValue()) {
		case "Kinetic Perimetry":
			KineticPerimetry kp=new KineticPerimetry();
			kp.show();
			break;
		case "Static Perimetry":
			StaticPerimetry sp=new StaticPerimetry();
			sp.show();
			break;
		}
	}
}
