package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<PowerOutages> getPoweroutagesByNerc(Nerc n){
		String sql="SELECT p.id, p.event_type_id, p.tag_id, p.area_id, p.nerc_id, p.responsible_id, p.customers_affected, p.date_event_began, p.date_event_finished, p.demand_loss "
				+ "FROM poweroutages AS p, nerc AS n "
				+ "WHERE p.nerc_id=n.id AND n.value=? ";
		List<PowerOutages> result = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, n.getValue());
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(new PowerOutages(rs.getInt("id"), rs.getInt("event_type_id"), rs.getInt("tag_id"), rs.getInt("area_id"), rs.getInt("nerc_id"), rs.getInt("responsible_id"), rs.getInt("customers_affected"), rs.getTimestamp("date_event_began").toLocalDateTime(), rs.getTimestamp("date_event_finished").toLocalDateTime(), rs.getInt("demand_loss")));
			}
			conn.close();
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	

}
