package com.jlcindia.bookstore.dao.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jlcindia.bookstore.to.UserTO;

@WebServlet(name = "myreg", urlPatterns = "/myreg.jlc")
public class RegisterServlet extends JLCBaseServlet {
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("RegisterServlet-service()");

        // 1. Collect the Input
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String dob = req.getParameter("dob");
        String gender = req.getParameter("gender");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 2. Validate the Input
        long phoneNumber = 0; // Default value for phone number
        if (phone != null && !phone.trim().isEmpty()) {
            try {
                phoneNumber = Long.parseLong(phone);
            } catch (NumberFormatException e) {
                // If the phone number is invalid, set an error message and forward to registration page
                req.setAttribute("RegMsg", "Invalid phone number format. Please enter a valid number.");
                req.getRequestDispatcher("register.jsp").forward(req, res);
                return; // Exit the method to prevent further processing
            }
        } else {
            // If the phone number is empty, set an error message and forward to registration page
            req.setAttribute("RegMsg", "Phone number is required.");
            req.getRequestDispatcher("register.jsp").forward(req, res);
            return; // Exit the method to prevent further processing
        }

        // 3. Process the Registration
        UserTO userTO = new UserTO();
        userTO.setFullName(fullName);
        userTO.setEmail(email);
        userTO.setPhone(phoneNumber);
        userTO.setDob(dob);
        userTO.setGender(gender);
        userTO.setUsername(username);
        userTO.setPassword(password);
        userTO.setStatus("Active");

        int x = userService.registerUser(userTO);

        // 4. Prepare to Forward
        String page = "";
        if (x == 1) {
            page = "login.jsp";
            String regMsg = "Congrats, Login first time";
            req.setAttribute("RegMsg", regMsg);
        } else {
            page = "register.jsp";
            String regMsg = "Registration failed, Try again";
            req.setAttribute("RegMsg", regMsg);
        }

        RequestDispatcher rd = req.getRequestDispatcher(page);
        rd.forward(req, res);
    }
}
