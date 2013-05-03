package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.exception.EmptyResultException;

public class DerbyDAOImpl implements CastDAO {

	static Logger logger = Logger.getLogger(DerbyDAOImpl.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	
	public DerbyDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
	}
	
	@Override
	public List<CharacterDTO> findAll() {
		ArrayList<CharacterDTO> cast = new ArrayList<CharacterDTO>();
		try{
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT MVCHAR_ID, MVCHAR_NAME, DIET, SOUNDS FROM TBL_CHARACTERS";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while(res.next()){
				int id = res.getInt("MVCHAR_ID");
				logger.info(" "+id);
				String name = res.getString("MVCHAR_NAME");
				logger.info(name);
				String diet = res.getString("DIET");
				logger.info(diet);
				String soundsArr= res.getString("SOUNDS");
				logger.info(soundsArr);
				logger.info(name+" "+diet+" "+soundsArr);
				String[] sounds = soundsArr.split(",");
				cast.add(new CharacterDTO(id, name, diet, sounds));
			}
			
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return cast;
	}
	
	
	@Override
	public void storeComment(CommentDTO comment) {
		
		PreparedStatement stmnt = null; 
		
		try{
			String sqlStr = 
				"INSERT INTO TBL_COMMENTS (MVCHAR_ID, MVCHAR_NAME, USERNAME, COMMENT_DATE, COMMENT) "+
				"VALUES (?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,comment.getCharacterID());
			stmnt.setString(2, comment.getCharacterName());
			stmnt.setString(3, comment.getUser());
			stmnt.setString(4, fmt.format(comment.getCommentDate()));
			stmnt.setString(5, comment.getComment());
			logger.info("sql string is "+stmnt.toString());
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to store comment! ");
			e.printStackTrace();
		}
		
	}
	
	public List<CommentDTO> getAllComments() {
		
		List<CommentDTO> comments = new ArrayList<CommentDTO>();
		try{
			Statement stmnt = connection.createStatement();
			ResultSet results = stmnt.executeQuery("SELECT * FROM TBL_COMMENTS");
			logger.info("Fetched comments");
			while(results.next()){
				int charID = results.getInt("MVCHAR_ID");
				String charName = results.getString("MVCHAR_NAME");
				Date commentDate = fmt.parse(results.getString("COMMENT_DATE"));
				String user = results.getString("USERNAME");
				String comment = results.getString("COMMENT");
				comments.add(new CommentDTO(charID, charName, user, commentDate, comment));
			}
			results.close();
			stmnt.close();
		}catch(Exception e){
			logger.severe("Failed to get comments "+e.getStackTrace());
		}
		
		return comments;
	}

	public CharacterDTO findChar(String value) throws EmptyResultException{
		
		CharacterDTO mvChar = null;
		
		try{
			logger.info("The value passed is: "+value);
			
			String count_query = "SELECT COUNT(*) FROM TBL_CHARACTERS WHERE MVCHAR_NAME = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, value);
			ResultSet count_res = count_stmnt.executeQuery();
			count_res.next();
			int numRows = count_res.getInt(1);
			logger.info("The result set size is "+numRows);
			//if(numRows==0) throw new EmptyResultException();
			
			String query_cast = "SELECT * FROM TBL_CHARACTERS WHERE MVCHAR_NAME = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setString(1, value);
			ResultSet res = stmnt.executeQuery();
			res.next();
			int id = res.getInt("MVCHAR_ID");
			logger.info(" "+id);
			String name = res.getString("MVCHAR_NAME");
			logger.info(name);
			String diet = res.getString("DIET");
			logger.info(diet);
			String soundsArr= res.getString("SOUNDS");
			logger.info(soundsArr);
			logger.info(name+" "+diet+" "+soundsArr);
			String[] sounds = soundsArr.split(",");
			mvChar = new CharacterDTO(id, name, diet, sounds);
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		
		return mvChar;
	}

	@Override
	public List<CommentDTO> getComments(String character) {
		List<CommentDTO> comments = new ArrayList<CommentDTO>();
		try{
			Statement stmnt = connection.createStatement();
			ResultSet results = stmnt.executeQuery("SELECT * FROM TBL_COMMENTS WHERE MVCHAR_NAME = '"+character+"'");
			logger.info("Fetched comments");
			while(results.next()){
				int charID = results.getInt("MVCHAR_ID");
				String charName = results.getString("MVCHAR_NAME");
				Date commentDate = fmt.parse(results.getString("COMMENT_DATE"));
				String user = results.getString("USERNAME");
				String comment = results.getString("COMMENT");
				comments.add(new CommentDTO(charID, charName, user, commentDate, comment));
			}
			results.close();
			stmnt.close();
		}catch(Exception e){
			logger.severe("Failed to get comments "+e.getStackTrace());
		}
		
		return comments;
	}
	

}
