package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author robertowm
 */
public class CreateArticleServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String text = request.getParameter("text");
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                fail(request, response);
            }
            UserKey userKey = (UserKey) user.getPrimaryKey();
            ArticleDataObject articleDataObject = new ArticleDataObject();
            articleDataObject.setTitle(title);
            articleDataObject.setText(text);
            articleDataObject.setUser(userKey);

            Object obj = EjbContext.instance().getContext().lookup("ArticleEJB");
            ArticleHome home = (ArticleHome) PortableRemoteObject.narrow(obj, ArticleHome.class);
            Article article = home.create(articleDataObject);

            if (article != null) {
                request.setAttribute("success", "Article posted.");
                getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                fail(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(CreateArticleServlet.class.getName()).log(Level.SEVERE, null, ex);
            fail(request, response);
        }
    }

    private void fail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("error", "Can't post the article.");
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
