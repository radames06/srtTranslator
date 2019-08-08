package com.jd.srtTranslator.dao;

import com.jd.srtTranslator.dao.DaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jd.srtTranslator.beans.SrtItem;

public class SrtFileDaoImpl implements SrtFileDao {
	private DaoFactory daoFactory;

	SrtFileDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void saveSrtFile(int fileId, List<SrtItem> SrtItems) throws DaoException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        try {
        	// First, we remove all data with the same fileId
        	clearFileDb(fileId);
            
            connexion = daoFactory.getConnection();
            
            for (SrtItem srtItem : SrtItems) {
                preparedStatement = connexion.prepareStatement("insert into srtContent (id_file, id_row, start, end, text, translation) values (?, ?, ?, ?, ?, ?);");
                preparedStatement.setString(1, Integer.toString(fileId));
                preparedStatement.setString(2, Integer.toString(srtItem.getRowId()));
                preparedStatement.setString(3, srtItem.getStart());
                preparedStatement.setString(4, srtItem.getEnd());
                preparedStatement.setString(5, srtItem.getText());
                preparedStatement.setString(6, srtItem.getTranslation());
                preparedStatement.executeUpdate();
            }
            
        } catch (SQLException e) {
            throw new DaoException("Impossible de communiquer avec la base de données : " + e.getMessage());
        }
        finally {
            try {
                if (connexion != null) {
                    connexion.close();  
                }
            } catch (SQLException e) {
                throw new DaoException("Impossible de communiquer avec la base de données : " + e.getMessage());
            }
        }
	}

	@Override
	public List<SrtItem> getSrtFile(int fileId) throws DaoException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        
        List<SrtItem> srtFile = new ArrayList<SrtItem>();
        
        try {
        	// First, we remove all data with the same fileId
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("select id_row, start, end, text, translation from srtContent where id_file=?;");
            preparedStatement.setString(1, Integer.toString(fileId));
            ResultSet resultat = preparedStatement.executeQuery();
            
            while (resultat.next()) {
                SrtItem srtItem = new SrtItem(
                		resultat.getInt("id_row"), 
                		resultat.getString("start"),
                		resultat.getString("end"),
                		resultat.getString("text"),
                		resultat.getString("translation"));
                srtFile.add(srtItem);
            }
        } catch (SQLException e) {
            throw new DaoException("Impossible de communiquer avec la base de données : " + e.getMessage());
        } finally {
            try {
                if (connexion != null) {
                    connexion.close();  
                }
            } catch (SQLException e) {
                throw new DaoException("Impossible de communiquer avec la base de données : " + e.getMessage());
            }
        }		
        return srtFile;
	}
	
	@Override
	public void clearFileDb( int fileId ) throws DaoException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
        try {
        	// First, we remove all data with the same fileId
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("delete from srtContent where id_file=?;");
            preparedStatement.setString(1, Integer.toString(fileId));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Impossible de communiquer avec la base de données : " + e.getMessage());
        }
        finally {
            try {
                if (connexion != null) {
                    connexion.close();  
                }
            } catch (SQLException e) {
                throw new DaoException("Impossible de communiquer avec la base de données : " + e.getMessage());
            }
        }
	}
}
