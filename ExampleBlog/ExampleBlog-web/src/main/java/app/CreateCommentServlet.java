package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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
public class CreateCommentServlet extends HttpServlet {

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
            Map t = request.getParameterMap();
            String strArticleID = request.getParameter("articleID");
            Integer articleID = Integer.parseInt(strArticleID);

            if (articleID == null) {
                fail(request, response);
            }

            Object obj = EjbContext.instance().getContext().lookup("ArticleEJB");
            ArticleHome home = (ArticleHome) PortableRemoteObject.narrow(obj, ArticleHome.class);
            Article article = home.findByPrimaryKey(new ArticleKey(articleID));

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String website = request.getParameter("website");
            String text = request.getParameter("text");

            if (article != null && article.commentArticle(name, email, website, text)) {
                request.setAttribute("article", article);
                request.setAttribute("error", "Article commented.");
                getServletContext().getRequestDispatcher("/article.jsp").forward(request, response);
            } else {
                fail(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(CreateCommentServlet.class.getName()).log(Level.SEVERE, null, ex);
            fail(request, response);
        }
    }

    private void fail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("error", "Can't comment this article.");
        getServletContext().getRequestDispatcher("/article.jsp").forward(request, response);
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
