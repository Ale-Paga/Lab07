package it.polito.tdp.poweroutages.model;

import java.util.Comparator;

public class ComparatorepowerOutages implements Comparator<PowerOutages>{

	@Override
	public int compare(PowerOutages o1, PowerOutages o2) {
		// TODO Auto-generated method stub
		//return o1.getYear().compareTo(o2.getYear());
		return o1.getDate_event_began().compareTo(o2.getDate_event_began());
	}

}
