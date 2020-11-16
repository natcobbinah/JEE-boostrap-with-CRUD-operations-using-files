package com.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class registration
 */
@WebServlet("/registration")
public class registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public registration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		// retrieve file content as a linked list string works
		LinkedList<String> registeredUsersList = new LinkedList<>();
		if (action != null) {
			FileReader in = new FileReader("RegistrationData.txt");
			BufferedReader freader = new BufferedReader(in);
			String line;
			while ((line = freader.readLine()) != null) {
				registeredUsersList.add(line);
			}
			freader.close();
			in.close();
		}
		request.setAttribute("users", registeredUsersList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/index.jsp";

		String btnsave = request.getParameter("save");

		// get the user data
		String surname = request.getParameter("surname");
		String firstname = request.getParameter("firstname");
		String gender = "";
		if (request.getParameter("gender") != null) {
			if (request.getParameter("gender").equals("Male")) {
				gender = "Male";
			}
			if (request.getParameter("gender").equals("Female")) {
				gender = "Female";
			}
		}
		String date = request.getParameter("dateofbirth");
		String email = request.getParameter("email");
		String confirmemail = request.getParameter("emailconfirm");
		String country = request.getParameter("country");

		/* Performing validation checks on form fields */
		String message = "";
		String emailMsg = "";
		String checkAge = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
		String currentDate = sdf.format(new Date());

		// performing form validation on form fields before saving to file if all
		// conditions are met
		if ((btnsave != null) && (email != confirmemail)) {
			emailMsg = "Email must be the same";
			url = "/index.jsp";
		}

		if ((btnsave != null)
				&& ((Integer.parseInt(currentDate.substring(0, 4)) - Integer.parseInt(date.substring(0, 4))) < 14)) {
			checkAge = "One must 15 years and above to register";
			url = "/index.jsp";
		} else if ((btnsave != null) && (surname == null || firstname == null || gender == null || date == null
				|| email == null || confirmemail == null || country == null || surname.isEmpty() || firstname.isEmpty()
				|| gender.isEmpty() || date.isEmpty() || confirmemail.isEmpty() || country.isEmpty())) {
			message = "One or more fields are required but empty";
			url = "/index.jsp";
		} else {
			// saving to textfile(Registration.txt)
			File file = new File("RegistrationData.txt");
			if (file.exists()) {
				System.out.println("exists");
			} else {
				file.createNewFile();
			}

			// write form content to file using filewriter works but prefer to save as //

			String formatSpace = "   ,";
			FileWriter out = new FileWriter(file, true);
			BufferedWriter writeTofile = new BufferedWriter(out);
			writeTofile.write(surname + formatSpace);
			writeTofile.write(firstname + formatSpace);
			writeTofile.write(gender + formatSpace);
			writeTofile.write(date + formatSpace);
			writeTofile.write(email + formatSpace);
			writeTofile.write(country);
			writeTofile.newLine();
			writeTofile.close();
			out.close();

			url = "/successPage.jsp";

			System.out.println("saved successfully");
		}

		String welcomeMsg = firstname + " " + surname;

		request.setAttribute("message", message);
		request.setAttribute("emailMsg", emailMsg);
		request.setAttribute("checkAge", checkAge);
		request.setAttribute("welcomeMsg", welcomeMsg);

		getServletContext().getRequestDispatcher(url).forward(request, response);
	}
}
