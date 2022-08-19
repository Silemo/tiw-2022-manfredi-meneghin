package it.polimi.tiw.projects.utils;

/**
 * Utility class for getting the path of the various HTML pages
 */
public class PathHelper {
	
    public static String pathToLoginPage             = "/WEB-INF/login.html";
    public static String pathToRegisterPage          = "/WEB-INF/register.html";
    public static String pathToInfoPane              = "/WEB-INF/infoPane.html";
	public static String pathToErrorPage             = "/WEB-INF/error.html";
    public static String pathToHomePage              = "/WEB-INF/home.html";
	public static String pathToAccountStatusPage     = "/WEB-INF/accountStatus.html";
    public static String pathToTransferFailedPage    = "/WEB-INF/transferFailed.html";
    public static String pathToTransferConfirmedPage = "/WEB-INF/transferConfirmed.html";
    
    
    public static String goToHomeServletPath              = "/GoToHome";
    public static String goToLoginServletPath             = "/GoToLoginPage";
    public static String goToTransferConfirmedServletPath = "/GoToTransferConfirmedPage";
}
