/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	double soglia = -1.0;
    	try {
    		soglia = Double.parseDouble(txtGoals.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("ERRORE - La soglia deve essere un valore numerico compreso fra 0 ed 1.");
    		return;
    	}
    	model.creaGrafo(soglia);
    	txtResult.appendText("Grafo creato con "+model.getNVertici()+" vertici e "+model.getNArchi()+" archi.\n");
   
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	int k = -1;
    	try {
    		k = Integer.parseInt(txtK.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("ERRORE - Il valore k deve essere numerico e positivo");
    		return;
    	}
    	
    	List<Player> dreamTeam = model.trovaDreamTeam(k);
    	txtResult.appendText(dreamTeam.toString());
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	Player best = model.getTopPlayer();
    	txtResult.appendText("Top Player: "+best.getName()+"\n");
    	List<Adiacenza> sconfitti = model.getSconfitti(best);
    	Collections.sort(sconfitti);
    	txtResult.appendText("Giocatori battuti; \n");
    	for(Adiacenza a:sconfitti) {
    		txtResult.appendText(a.getP2()+" : "+a.getPeso()+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
