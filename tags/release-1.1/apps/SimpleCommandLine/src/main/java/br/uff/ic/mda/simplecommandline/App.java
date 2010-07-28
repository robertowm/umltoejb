package br.uff.ic.mda.simplecommandline;

import br.uff.ic.mda.transformer.EjbCodeGenerator;
import br.uff.ic.mda.transformer.EjbDomain;
import br.uff.ic.mda.transformer.TransformationContract;
import br.uff.ic.mda.transformer.UmlDomain;
import br.uff.ic.mda.transformer.UmlEjbDomain;
import br.uff.ic.mda.transformer.UmlEjbTransformer;
import core.XEOS;
import core.exception.XEOSException;

/**
 * Hello world!
 *
 */
public class App {

    private static XEOS xeos;
    private static UmlDomain umlDomain;
    private static EjbDomain ejbDomain;
    private static UmlEjbDomain joinedDomain;

    public static void main(String[] args) throws Exception {
        xeos = new XEOS();

        {
            xeos.createClassDiagram();

            umlDomain = new UmlDomain(xeos);
            ejbDomain = new EjbDomain(xeos);
            joinedDomain = new UmlEjbDomain(xeos);

            umlDomain.insertMetamodelClasses();
            ejbDomain.insertMetamodelClasses();
            joinedDomain.insertMetamodelClasses();

            umlDomain.insertMetamodelAttributes();
            ejbDomain.insertMetamodelAttributes();
            joinedDomain.insertMetamodelAttributes();

            umlDomain.insertMetamodelAssociations();
            ejbDomain.insertMetamodelAssociations();
            joinedDomain.insertMetamodelAssociations();

            umlDomain.insertMetamodelOperations();
            ejbDomain.insertMetamodelOperations();
            joinedDomain.insertMetamodelOperations();

            umlDomain.insertMetamodelInvariants();
            ejbDomain.insertMetamodelInvariants();
            joinedDomain.insertMetamodelInvariants();

            xeos.closeClassDiagram();
        }

        {
            xeos.createObjectDiagram();

            umlDomain.createSpecificationOfCurrentDiagram();
            ejbDomain.createSpecificationOfCurrentDiagram();
            joinedDomain.createSpecificationOfCurrentDiagram();

            insertBlogDiagram();

            TransformationContract ct = new TransformationContract(umlDomain, ejbDomain, joinedDomain, new UmlEjbTransformer(umlDomain, ejbDomain, joinedDomain), new EjbCodeGenerator(ejbDomain, ""));
            ct.transform();

            xeos.closeObjectDiagram();
        }
    }

    private static void insertBlogDiagram() throws Exception {
        umlDomain.insertClass("User_ID", "User");
        umlDomain.insertClass("Article_ID", "Article");
        umlDomain.insertClass("Comment_ID", "Comment");

        umlDomain.insertAttribute("firstName_u_ID", "firstName", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("lastName_u_ID", "lastName", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("nickName_u_ID", "nickName", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("login_u_ID", "login", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("password_u_ID", "password", "public", "UMLString", "User_ID");
        umlDomain.insertAttribute("email_u_ID", "email", "public", "UMLString", "User_ID");

        umlDomain.insertAttribute("title_a_ID", "title", "public", "UMLString", "Article_ID");
        umlDomain.insertAttribute("text_a_ID", "text", "public", "UMLString", "Article_ID");

        umlDomain.insertAttribute("name_c_ID", "name", "public", "UMLString", "Comment_ID");
        umlDomain.insertAttribute("email_c_ID", "email", "public", "UMLString", "Comment_ID");
        umlDomain.insertAttribute("website_c_ID", "website", "public", "UMLString", "Comment_ID");
        umlDomain.insertAttribute("text_c_ID", "text", "public", "UMLString", "Comment_ID");

        umlDomain.insertAssociationEnd("user_ua_ID", "user", "public", "User_ID", "1", "1", false, "Article_ID");
        umlDomain.insertAssociationEnd("article_ua_ID", "articles", "public", "Article_ID", "0", "*", false, "User_ID");
        umlDomain.insertLinksBetweenAssociationEnds("user_ua_ID", "article_ua_ID");
        umlDomain.insertAssociation("user_article_ID", "has", "user_ua_ID", "article_ua_ID");

        umlDomain.insertAssociationEnd("article_ac_ID", "article", "public", "Article_ID", "1", "1", true, "Comment_ID");
        umlDomain.insertAssociationEnd("comment_ac_ID", "comments", "public", "Comment_ID", "0", "*", false, "Article_ID");
        umlDomain.insertLinksBetweenAssociationEnds("article_ac_ID", "comment_ac_ID");
        umlDomain.insertAssociation("article_comment_ID", "has", "article_ac_ID", "comment_ac_ID");

        umlDomain.insertOperation("findByLoginAndPassword_ID", "findByLoginAndPassword", "public", "User_ID", "User_ID");
        umlDomain.insertParameter("flp_login_ID", "login", "UMLString", "findByLoginAndPassword_ID");
        umlDomain.insertParameter("flp_password_ID", "password", "UMLString", "findByLoginAndPassword_ID");

        umlDomain.insertOperation("postNewArticle_ID", "postNewArticle", "public", "UMLBoolean", "User_ID");
        umlDomain.insertParameter("pna_title_ID", "title", "UMLString", "postNewArticle_ID");
        umlDomain.insertParameter("pna_text_ID", "text", "UMLString", "postNewArticle_ID");

        umlDomain.insertOperation("commentArticle_ID", "commentArticle", "public", "UMLBoolean", "Article_ID");
        umlDomain.insertParameter("ca_name_ID", "name", "UMLString", "commentArticle_ID");
        umlDomain.insertParameter("ca_email_ID", "email", "UMLString", "commentArticle_ID");
        umlDomain.insertParameter("ca_website_ID", "website", "UMLString", "commentArticle_ID");
        umlDomain.insertParameter("ca_text_ID", "text", "UMLString", "commentArticle_ID");

        umlDomain.insertSet("setArticles_ID", "SetArticle", "Article_ID");
        umlDomain.insertOperation("findAllArticles_ID", "findAll", "public", "setArticles_ID", "Article_ID");
    }
}
