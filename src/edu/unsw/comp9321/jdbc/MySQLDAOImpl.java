package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
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

public class MySQLDAOImpl implements CastDAO {

	static Logger logger = Logger.getLogger(MySQLDAOImpl.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public MySQLDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
	}
	
	@Override
	public List<CharacterDTO> findAll() {
		ArrayList<CharacterDTO> cast = new ArrayList<CharacterDTO>();
		try{
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT NAME, DIET, SOUNDS FROM TBL_CHARACTERS";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while(res.next()){
				String name = res.getString("NAME");
				logger.info(name);
				String diet = res.getString("DIET");
				logger.info(diet);
				String soundsArr= res.getString("SOUNDS");
				logger.info(soundsArr);
				logger.info(name+" "+diet+" "+soundsArr);
				String[] sounds = soundsArr.split(",");
				//cast.add(new CharacterDTO(name, diet, sounds));
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
		
		Statement stmnt = null; 
		
		try{
			stmnt = connection.createStatement();
			String sqlStr = "INSERT INTO TBL_COMMENTS (MVCHAR_ID, MVCHAR_NAME, USERNAME, COMMENT_DATE, COMMENT) VALUES ("
				+comment.getCharacterID()+","
				+"'"+comment.getCharacterName()+"',"
				+"'"+comment.getUser()+"',"
				+"'"+fmt.format(comment.getCommentDate())+"',"
				+"'"+comment.getComment()+"')";
			logger.info("sql string is "+sqlStr);
			int result = stmnt.executeUpdate(sqlStr);
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

	@Override
	public CharacterDTO findChar(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommentDTO> getComments(String character) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
