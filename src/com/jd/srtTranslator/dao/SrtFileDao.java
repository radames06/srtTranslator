package com.jd.srtTranslator.dao;

import java.util.List;

import com.jd.srtTranslator.beans.SrtItem;

public interface SrtFileDao {
	void saveSrtFile( int fileId, List<SrtItem> SrtItems ) throws DaoException;
	List<SrtItem> getSrtFile( int fileId ) throws DaoException;
	void clearFileDb( int fileId ) throws DaoException;
}
