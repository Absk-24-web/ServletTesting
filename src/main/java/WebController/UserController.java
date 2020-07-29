package WebController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Comman.ExcleDownload;
import Comman.PDF_Download;
import Comman.PasswordEncryptDcrypt;
import Dao.UserDao;
import Model.User;

@WebServlet("/")
@MultipartConfig
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDao;
	private ExcleDownload excelDownload;
	private PDF_Download pdf_Download; 
	private PasswordEncryptDcrypt passwordEncryptDcrypt;

	public void init() {
		userDao = new UserDao();
		excelDownload = new ExcleDownload();
		pdf_Download = new PDF_Download();
		passwordEncryptDcrypt = new	PasswordEncryptDcrypt();
		}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
	    switch (action) {
		case "/new":
			showNewForm(request, response);
			break;
		case "/insert":
			insertUser(request, response);
			break;
		case "/edit":
			showEditForm(request, response);
			break;	
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case "/update":
			updateUser(request, response);
			break;
		case "/excel":
			exportExcel(request, response);
			break;
		case "/pdf":
			exportPdf(request, response);
			break;
		default:
			try {
				listUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
	}
	}
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDao.getUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
		
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = passwordEncryptDcrypt.encrypt(request.getParameter("password"));
		User user = new User(id, name, email, password);
		userDao.updateUser(user);
		response.sendRedirect("list");
		
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDao.deleteUser(id);
		response.sendRedirect("list");
		
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = passwordEncryptDcrypt.encrypt(request.getParameter("password"));
		User user = new User(name, email, password);
		userDao.registerUser(user);
		response.sendRedirect("list");
		
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		List<User> listUser = userDao.getAllUser();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void exportExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 excelDownload.export();
		 response.sendRedirect("list");
		
	}
	private void exportPdf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 pdf_Download.export_PDF();
		 response.sendRedirect("list");
		
	}

}
