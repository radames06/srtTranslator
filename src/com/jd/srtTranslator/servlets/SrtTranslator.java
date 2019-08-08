package com.jd.srtTranslator.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.jd.srtTranslator.beans.SrtItem;
import com.jd.srtTranslator.dao.DaoException;
import com.jd.srtTranslator.dao.DaoFactory;
import com.jd.srtTranslator.dao.SrtFileDao;;

public class SrtTranslator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int currentStep = 1;
	private SrtFileDao srtFileDao;
	private List<SrtItem> srtFile;
	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.srtFileDao = daoFactory.getSrtFileDao();
	}

	public SrtTranslator() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			queryFile();
			if (this.srtFile.size() != 0) {
				currentStep = 2;
				request.setAttribute("srtFile", srtFile.toArray());
			} else {
				currentStep = 1;
			}
		} catch (DaoException e) {
			currentStep = 1;
			request.setAttribute("Step1Error", e.getMessage());
		}
		request.setAttribute("currentStep", currentStep);
		this.getServletContext().getRequestDispatcher("/WEB-INF/srtTranslator.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream out;
		
		int actionStep = Integer.parseInt(request.getParameter("actionStep"));
		switch (actionStep) {
		case 1: // Post a new file to be saved in Database (Step 1 in the main form)
			try {
				saveFile(request, response);
				currentStep = 2;
				request.setAttribute("srtFile", srtFile.toArray());
			} catch (DaoException e) {
				currentStep = 1;
				request.setAttribute("Step1Error", e.getMessage());
			}
			request.setAttribute("currentStep", currentStep);
			this.getServletContext().getRequestDispatcher("/WEB-INF/srtTranslator.jsp").forward(request, response);

			break;
		case 2: // Post updates on translations (Step 2 in the main form)
			int i = 0;
			for (SrtItem srtItem : srtFile) {
				String translation = request.getParameter("translation" + Integer.toString(i));
				if (translation == null) translation = "";
				i++;
				srtItem.setTranslation(translation);
			}
			request.setAttribute("srtFile", srtFile.toArray());
			try {
				this.srtFileDao.saveSrtFile(1, srtFile);
				currentStep = 3;
			} catch (DaoException e) {
				currentStep = 2;
				request.setAttribute("Step2Error", e.getMessage());
			}
			request.setAttribute("currentStep", currentStep);
			this.getServletContext().getRequestDispatcher("/WEB-INF/srtTranslator.jsp").forward(request, response);

			break;
		case 3: // Download translated file (Step 3 in the main form)
			response.setContentType("text/plain");
			response.setHeader("Content-disposition", "attachment; filename=yourSRTFile.srt");

			out = response.getOutputStream();
			
			for (SrtItem srtItem : srtFile) {
				String buffer = Integer.toString(srtItem.getRowId()) + "\n";
				out.write(buffer.getBytes());
				buffer = srtItem.getStart() + " --> " + srtItem.getEnd() + "\n";
				out.write(buffer.getBytes());
				buffer = srtItem.getTranslation() + "\n";
				out.write(buffer.getBytes());
				buffer = "\n";
				out.write(buffer.getBytes());
			}
			
			out.flush();
			out.close();

			break;
		case 4: // Download file containing original texts (Step 3 in the main form)
			response.setContentType("text/plain");
			response.setHeader("Content-disposition", "attachment; filename=SRTFile.srt");

			out = response.getOutputStream();
			
			for (SrtItem srtItem : srtFile) {
				String buffer = Integer.toString(srtItem.getRowId()) + "\n";
				out.write(buffer.getBytes());
				buffer = srtItem.getStart() + " --> " + srtItem.getEnd() + "\n";
				out.write(buffer.getBytes());
				buffer = srtItem.getText() + "\n";
				out.write(buffer.getBytes());
				buffer = "\n";
				out.write(buffer.getBytes());
			}
			
			out.flush();
			out.close();

			break;
		case 5: // Clear database (Step 4 in the main form)
			try {
				this.srtFileDao.clearFileDb(1);
				this.srtFile = new ArrayList<SrtItem>();
				currentStep = 1;
			} catch (DaoException e) {
				request.setAttribute("Step4Error", e.getMessage());
			}
			request.setAttribute("currentStep", currentStep);
			request.setAttribute("srtFile", srtFile.toArray());
			this.getServletContext().getRequestDispatcher("/WEB-INF/srtTranslator.jsp").forward(request, response);

			break;
		}
	}

	// Upload file
	private void saveFile(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, DaoException {

		srtFile = new ArrayList<SrtItem>();
		Part part = request.getPart("srtFile");
		InputStream filecontent = part.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(filecontent));
		String idLine;
		String timeLine;
		String text1;
		String text2;
		String text;

		do {
			SrtItem srtItem = new SrtItem();

			idLine = reader.readLine();
			timeLine = reader.readLine();
			text1 = reader.readLine();
			text2 = reader.readLine();
			if (text2 != null) {
				if (!text2.equals("")) {
					reader.readLine();
					text = text1 + "\n" + text2;
				} else {
					text = text1;
				}
			} else {
				text = text1;
			}
			if (idLine != null) {
				//System.out.println(idLine);
				srtItem.setRowId(Integer.parseInt(idLine));
				srtItem.setStart(timeLine.substring(0, 12));
				srtItem.setEnd(timeLine.substring(17, 29));
				srtItem.setText(text);
				srtItem.setTranslation("");
				srtFile.add(srtItem);
			}
		} while (text2 != null);
		this.srtFileDao.saveSrtFile(1, srtFile);
	}

	private void queryFile() throws DaoException {
		this.srtFile = this.srtFileDao.getSrtFile(1);
	}
}
