package ganji;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class editServlet extends HttpServlet {
	Key key=null;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		// get access to the user service to get our user
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		key =(Key) req.getSession().getAttribute("update");

		try{
		if (u != null) {
			req.setAttribute("loggedin", 1);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Task t = pm.getObjectById(Task.class, key);
			req.setAttribute("task", t.getName());
			req.setAttribute("date", t.getDate());
			req.getSession().setAttribute("update",null);


		} else {
			req.setAttribute("loggedin", null);
		}
		}catch(Exception e){resp.sendRedirect("/");}

		String login_url = us.createLoginURL("/");
		req.setAttribute("user", u);
		req.setAttribute("login_url", login_url);

		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/edit.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		// get access to the user service to get our user
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();

		String n = req.getParameter("task").trim();
		String d = req.getParameter("date").trim();
		if (n.equals("") || d.equals("")) {
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Wrong Input !');");
			out.println("location='/editServlet'");
			out.println("</script>");

			return;
		}
		String uid = u.getUserId();
		PersistenceManager pm = null;
		Key user_key = KeyFactory.createKey("User", uid);
		ganji.User user;
		try {
			// Check if the KEY exists...
			pm = PMF.get().getPersistenceManager();
			Task ap = pm.getObjectById(Task.class, key);
			ap.setName(n);
			ap.setDate(d);
			pm.close();
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Task updated !');");
			out.println("location='/'");
			out.println("</script>");

		} catch (Exception e) {
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Update Failed !');");
			out.println("location='/'");
			out.println("</script>");
		} finally {
			pm.close();
		}
	}

}
