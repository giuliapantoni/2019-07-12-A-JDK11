package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getFoodsByPortions(int portions){
		String sql = "SELECT food.food_code, food.display_name, COUNT(DISTINCT portion.portion_id) AS cnt " + 
				"FROM food, `portion` " + 
				"WHERE food.food_code = portion.food_code " + 
				"GROUP BY food.food_code " + 
				"HAVING cnt = ? " + 
				"ORDER BY food.display_name ASC ";
		List<Food> result = new ArrayList<Food>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, portions);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				result.add(new Food(rs.getInt("food_code"), rs.getString("display_name")));
			}
			conn.close();
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public Double calorieCongiunte(Food f1, Food f2) {
		
		String sql = "SELECT fc1.food_code, fc2.food_code, AVG(condiment.condiment_calories) as avg " + 
				"FROM food_condiment AS fc1, food_condiment as fc2, condiment " + 
				"WHERE fc1.condiment_code = fc2.condiment_code " + 
				"AND fc1.condiment_code = condiment.condiment_code " + 
				"AND fc1.id <> fc2.id " + 
				"AND fc1.food_code = ? AND fc2.food_code = ? " + 
				"GROUP BY fc1.food_code, fc2.food_code " ;
		Double peso = null ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, f1.getFood_code());
			st.setInt(2, f2.getFood_code());
			ResultSet rs = st.executeQuery();
			if(rs.first())
				peso = rs.getDouble("avg");
			conn.close();
			return peso;
		}catch(SQLException e ) {
			e.printStackTrace();
		}
		return peso;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
