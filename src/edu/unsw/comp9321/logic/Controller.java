package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.common.ServiceLocatorException;
import edu.unsw.comp9321.exception.EmptyResultException;
import edu.unsw.comp9321.exception.InvalidActionException;
import edu.unsw.comp9321.jdbc.CastDAO;
import edu.unsw.comp9321.jdbc.CommentDTO;
import edu.unsw.comp9321.jdbc.DerbyDAOImpl;
import edu.unsw.comp9321.jdbc.MySQLDAOImpl;
import edu.unsw.comp9321.jdbc.CharacterDTO;

/**
 * Servlet implementation class Controller
 */
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(Controller.class.getName());
	private CastDAO cast;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public Controller() throws ServletException {
    	// TODO Auto-generated constructor stub
        super();
        try {
			cast = new DerbyDAOImpl();
		} catch (ServiceLocatorException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		} catch (SQLException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPage = "";
		String action = request.getParameter("action");
		String character = request.getParameter("character");
		
		try{
			if((action==null)||(character==null)){
				throw new InvalidActionException();
			}
			if(action.equals("info")){
				request.setAttribute("mcharacter", cast.findChar(character));
				request.setAttribute("character", character);
				forwardPage = "cast.jsp";
			}else if(action.equals("comments")){
				request.setAttribute("comments", cast.getComments(character));
				request.setAttribute("character", character);
				forwardPage ="comments.jsp";
			}else if(action.equals("postcomment"))
				forwardPage = handlePostcomment(request,response);
			else forwardPage = "error.jsp";
		}catch(EmptyResultException e){
			String message = "Character was not found. Please go back and try again.";
			request.setAttribute("message", message);
			forwardPage = "error.jsp";
		} catch (InvalidActionException e) {
			String message = "Action or character was not selected. Please go back and try again.";
			request.setAttribute("message", message);
			forwardPage = "error.jsp";
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
	}
	
	private String handlePostcomment(HttpServletRequest request, HttpServletResponse response){
		String forwardPage = "";
		String character = (String) request.getParameter("character");
		logger.info("Comment on character: "+character);
		try{
			CharacterDTO mchar = cast.findChar(character);
			String commentString = request.getParameter("comments");
			CommentDTO comment = new CommentDTO(mchar.getId(), mchar.getName(), "SKV", new Date(), commentString);
			cast.storeComment(comment);
			request.setAttribute("comments", cast.getComments(character));
			forwardPage = "success.jsp";
		}catch(Exception e){
			e.printStackTrace();
			forwardPage = "error.jsp";
		}
		return forwardPage;
	}

}
