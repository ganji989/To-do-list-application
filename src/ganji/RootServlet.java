package ganji;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// we need to get access to the google user service
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		String login_url = us.createLoginURL("/");
		String logout_url = us.createLogoutURL("/");
		// attach a few things to the request such that we can access them in
		// the jsp
		req.setAttribute("user", u);
		req.setAttribute("login_url", login_url);
		req.setAttribute("logout_url", logout_url);
		String addTask = "/add";
		req.setAttribute("addLink", addTask);

		String uid;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			if (u != null) {
				uid = u.getUserId();
				// displaying current appointments
				Key user_key = KeyFactory.createKey("User", uid);
				ganji.User user;
				// RETRIEVE USER
				user = pm.getObjectById(ganji.User.class, user_key);
				String current_tasks = "";
				current_tasks = "<div id= \"app\" class=\"boxed1\">\n" + "<form method=\"post\">\n"
						+ "<h2>~Your Current To-do List~</h2><br/>\n";
				for (int i = 0; i < user.getTasks().size(); i++) {
					Task ctemp = user.getTasks().get(i);
					if (ctemp.isChecked())
						current_tasks += "<input type=\"checkbox\" name=\"" + i + "complete\" checked>";
					else
						current_tasks += "<input type=\"checkbox\" name=\"" + i + "complete\" >";

					current_tasks += "  TASK : " + ctemp.getName() + "  DATE : " + ctemp.getDate()
							+ "  -----  <input type=\"submit\" name=\"" + (i + "e")
							+ "\" value=\"Edit\" \\><input type=\"submit\" name=\"" + (i + "d")
							+ "\" value=\"Delete\" \\></br>";
				}
				current_tasks += "</br><input type=\"submit\" style=\"height:8em; width:10em\" name=\"update\" value=\"update\" \\></br></br><br/><br/></form></div>";
				req.setAttribute("to-do-list", current_tasks);
			}
		} catch (Exception e) {
			req.setAttribute("to-do-list", null);
			uid = u.getUserId();
			Key user_key = KeyFactory.createKey("User", uid);
			ganji.User user = new ganji.User(user_key, u.getEmail());
			pm.makePersistent(user);

		} finally {
			pm.close();
		}

		// get a request dispatcher and launch a jsp that will render our page
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// we need to get access to the google user service
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		if (u == null)
			return;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key user_key = KeyFactory.createKey("User", u.getUserId());
		ganji.User user = pm.getObjectById(ganji.User.class, user_key);

		// updating status
		if (req.getParameter("update") != null) {
			if (user.getTasks().size() == 0) {
				PrintWriter out = resp.getWriter();
				out.println("<script type=\"text/javascript\">");
				out.println("alert('" + "Nothing to update !" + "');");
				out.println("location=" + "'/'" + ";");
				out.println("</script>");
			}
			for (int i = 0; i < user.getTasks().size(); i++) {
				boolean status;
				if (req.getParameter(i + "complete") != null)
					status = true;
				else
					status = false;

				updateTaskStatus(user.getTasks().get(i).getId(), status, resp.getWriter());
			}
			return;
		}

		for (int i = 0; i < user.getTasks().size(); i++) {
			if (req.getParameter((i + "d")) != null) {
				// delete this user
				pm.deletePersistent(user.getTasks().get(i));
				pm.close();
				PrintWriter out = resp.getWriter();
				out.println("<script type=\"text/javascript\">");
				out.println("alert('" + "Task Deleted !" + "');");
				out.println("location=" + "'/'" + ";");
				out.println("</script>");
				break;
			} else if (req.getParameter((i + "e")) != null) {
				// Edit this appointment
				req.getSession().setAttribute("update", user.getTasks().get(i).getId());
				resp.sendRedirect("/editServlet");
				break;
			}
		}
	}

	public void updateTaskStatus(Key key, boolean status, PrintWriter out) {
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		String uid = u.getUserId();
		PersistenceManager pm = null;
		Key user_key = KeyFactory.createKey("User", uid);
		try {
			// Check if the KEY exists...
			pm = PMF.get().getPersistenceManager();
			Task t = pm.getObjectById(Task.class, key);
			t.setStatus(status);
			pm.close();
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Status Updated !');");
			out.println("location=" + "'/'" + ";");
			out.println("</script>");

		} catch (Exception e) {
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Updation Failed !');");
			out.println("location=" + "'/'" + ";");
			out.println("</script>");
		} finally {
			pm.close();
		}
	}

}