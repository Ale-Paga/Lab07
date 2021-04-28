/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	try {
    		Nerc n = this.cmbNerc.getValue();
        	int year= Integer.parseInt(this.txtYears.getText());
        	int hours = Integer.parseInt(this.txtHours.getText());
        	
        	List<PowerOutages> result = model.ottimizzaBlackout(n, hours, year);
        	
        	this.txtResult.appendText("Tot people affected:" + model.sommaClienti(result)+"\n");
        	int totHours=model.sommaMinuti(result)/60;
        	this.txtResult.appendText("Tot hours of outage: "+totHours+"\n");
        	
        	for(PowerOutages p : result) {
        		this.txtResult.appendText(p.getYear()+" "+p.getDate_event_began()+" "+p.getDate_event_finished()+" "+p.getCustomer_affected()+"\n");
        	}
    	}catch (NullPointerException npe){
    		this.txtResult.setText("ERRORE: devi selezionare un Nerc \n");
    		return;
    	}catch (NumberFormatException nfe) {
    		this.txtResult.setText("ERRORE: non inserire lettere \n");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbNerc.getItems().addAll(this.model.getNercList());
    }
}
