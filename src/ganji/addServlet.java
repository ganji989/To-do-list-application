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
public class addServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// we will be outputting html to the client
		resp.setContentType("text/html");
		// get access to the user service to get our user
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		// get the value of the attribute input from the session and forward it
		// to the // JSP
		//String uid = (String) req.getSession().getAttribute("user_id");
		if (u != null ) {
			req.setAttribute("loggedin", 1);
		} else {
			req.setAttribute("loggedin", null);
		}
		String login_url = us.createLoginURL("/");
		req.setAttribute("user", u);
		req.setAttribute("login_url", login_url);
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/add.jsp");
		rd.forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		// get access to the user service to get our user
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();

		String n = req.getParameter("name").trim();
		String d = req.getParameter("date").trim();
		if (n.equals("") || d.equals("")) {
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Wrong Input !');");
			out.println("location='/addServlet';");
			out.println("</script>");
			return;
		}
		String uid = u.getUserId();
		PersistenceManager pm = null;
		Key user_key = KeyFactory.createKey("User", uid);
		ganji.User user;
		Task task = new Task(n,d);

		try {
			
			// Check if the KEY exists...
			pm = PMF.get().getPersistenceManager();
			user = pm.getObjectById(ganji.User.class, user_key);
			task.setParent(user);
			user.addTask(task);
			pm.makePersistent(task);
			pm.makePersistent(user);
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Task Added !');");
			out.println("location='/';");
			out.println("</script>");

		} catch (Exception e) {
			// This user doesnt exist yet, so add it...
			user = new ganji.User(user_key, u.getEmail());
			pm.makePersistent(user);
			pm.close();
			// Now get user
			pm = PMF.get().getPersistenceManager();
			user = pm.getObjectById(ganji.User.class, user_key);
			task.setParent(user);
			user.addTask(task);
			pm.makePersistent(task);
			pm.makePersistent(user);
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Task Added !');");
			out.println("location='/';");
			out.println("</script>");
		} finally {
			pm.close();
		}
	}
}
