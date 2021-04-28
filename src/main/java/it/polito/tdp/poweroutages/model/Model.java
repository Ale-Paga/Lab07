package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	List<PowerOutages> best;
	List<PowerOutages> totBlackout;
	int bestAffected;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<PowerOutages> ottimizzaBlackout(Nerc n, int ore, int anni){
		List<PowerOutages> parziale = new ArrayList<>();
		this.totBlackout= podao.getPoweroutagesByNerc(n);
		Collections.sort(totBlackout, new ComparatorepowerOutages());
		best=null;
		int minuti = ore * 60;
		this.bestAffected=0;
		cerca(parziale, 0, minuti, anni);
		
		return best;
	}

	private void cerca(List<PowerOutages> parziale, int livello, int minuti, int anni) {
		
		//caso terminale/controllo ore
		/*int parzialeMin = this.sommaMinuti(parziale);
		if(parzialeMin>minuti) {
			return;
		}*/
		
		//controllo migliore
		int parzialeAffected = this.sommaClienti(parziale);
		if(parzialeAffected>this.bestAffected) {
			this.bestAffected=parzialeAffected;
			this.best= new ArrayList<>(parziale); //sovrascrivo
		}
		
		//ricorsione
		for(PowerOutages p: this.totBlackout) {
			if(!parziale.contains(p)) {
				parziale.add(p);
				if(parziale_isValida(parziale, minuti, anni)) {
					cerca(parziale, livello+1, minuti, anni);
				}
				parziale.remove(p);
			}
		}
	}

	private boolean parziale_isValida(List<PowerOutages> parziale, int minuti, int anni) {
		int sommaMin=0;
		for(PowerOutages p: parziale) {
			sommaMin+=p.getOutage_duration();
		}
		
		if(sommaMin>minuti) {
			return false;
		}
		
		if( ((parziale.get(parziale.size()-1).getYear()) - (parziale.get(0).getYear()))>anni  ) {        
			return false;
		}
		return true;
	}

	
	
	
	public int sommaMinuti(List<PowerOutages> parziale) {
		int somma=0;
		for(PowerOutages p: parziale) {
			somma+=p.getOutage_duration();
		}
		return somma;
	}
	
	public int sommaClienti(List<PowerOutages> parziale) {
		int somma=0;
		for(PowerOutages p: parziale) {
			somma+=p.getCustomer_affected();
		}
		return somma;
	}

}
