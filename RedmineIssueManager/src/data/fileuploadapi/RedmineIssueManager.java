package data.fileuploadapi;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import java.util.logging.Logger;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
@Path("/redmine")
public class RedmineIssueManager {
	@SuppressWarnings("unused")
	@Path("{choice}")
    @GET   // this method process GET request from client
    
    @Produces("application/json")   // sends JSON
	
    
    
    
   public Response getJson( @PathParam("choice") String choice) throws RedmineException {  // empno represents the empno sent from client   
    	String uri = "http://10.4.6.10/redmine/";
        String uname = "admin";
        String password = "admin";
        
        String  projectKey = choice.toLowerCase(); // any
        Integer queryId = null; // any
        

    	RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(uri,uname,password);
    	    IssueManager issueManager = mgr.getIssueManager();
    	    UserManager usermanager=mgr.getUserManager();
	
    	   
        	  
        	 
    	    String tmp11 = "";
			List<Issue> issues = issueManager.getIssues(projectKey, queryId);
			for (Issue issue : issues) {
				Tracker tracker=issue.getTracker();
				
				tmp11=tmp11+issue+" status = "+issue.getStatusName()+" priority = "+issue.getPriorityText()+" Tracker = "+tracker.getName();
				tmp11=tmp11+"\n";
			}
    	        JSONObject jsonObject5 = new JSONObject();
				   jsonObject5.put("details", tmp11);
				   String result5 = "" + jsonObject5;
				   return Response.status(200).entity(result5).build();
      	    
				   
			
				  
    	}// end of getJson()
     
	
}
