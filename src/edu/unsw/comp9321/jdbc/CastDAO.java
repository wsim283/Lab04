package edu.unsw.comp9321.jdbc;

import java.util.List;

import edu.unsw.comp9321.exception.EmptyResultException;

public interface CastDAO {
	
	public List<CharacterDTO> findAll();
	
	public void storeComment(CommentDTO comment);
	
	public List<CommentDTO> getAllComments();

	public CharacterDTO findChar(String value) throws EmptyResultException;

	public List<CommentDTO> getComments(String character);

}
