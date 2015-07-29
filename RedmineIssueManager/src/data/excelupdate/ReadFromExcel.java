package data.excelupdate;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.IssueStatus;
import com.taskadapter.redmineapi.bean.IssueStatusFactory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.TrackerFactory;
import com.taskadapter.redmineapi.bean.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
@Path("/redmine1/")
public class ReadFromExcel {
	@SuppressWarnings("unused")
	@Path("{file}")
    @GET   // this method process GET request from client
    
    @Produces("application/json")   // sends JSON
	
    
    
    
   public Response getJson1( @PathParam("file") String file) throws RedmineException, BiffException, IOException {  // empno represents the empno sent from client   
    	String uri = "http://10.4.6.10/redmine/";
        String uname = "admin";
        String password = "admin";
       
        Integer queryId = null; // any
        
System.out.println("working"+file);
    	RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(uri,uname,password);
    	    IssueManager issueManager = mgr.getIssueManager();
    	    UserManager usermanager=mgr.getUserManager();
    	    ProjectManager prjmgr=mgr.getProjectManager();
    	
    	    
        	  
        	    String FilePath = "/home/user/Documents/"+file; //Enter the path to be stored
        		FileInputStream fs = new FileInputStream(FilePath);
        		System.out.println(FilePath);
        		Workbook wb = Workbook.getWorkbook(fs);

        		// TO get the access to the sheet
        		Sheet sh = wb.getSheet("Sheet1");

        		// To get the number of rows present in sheet
        		int totalNoOfRows = sh.getRows();

        		// To get the number of columns present in sheet
        		int totalNoOfCols = sh.getColumns();

        		for (int row = 0; row < totalNoOfRows; row++) {
        			int pid=0;
        			for (int col = 0; col < totalNoOfCols; col++) {
        				
        				if(sh.getCell(col, row).getContents().equals("create"))
        				{
        					List<Project> prj= prjmgr.getProjects();
        					for (Project project : prj){
        						if(project.getName().equals(sh.getCell(col-1,row).getContents()))
        							 pid=project.getId();
        						
        						}
        					
        					Issue issueToCreate = IssueFactory.create(pid,sh.getCell(col+1, row).getContents());
        	        	    Issue createdIssue = issueManager.createIssue(issueToCreate);
        	        	   int id=createdIssue.getId();
        	        	   Issue iss=issueManager.getIssueById(id);
         	        	   iss.setPriorityId(Integer.parseInt(sh.getCell(col+4,row).getContents()));
         	        	  Tracker tracker=TrackerFactory.create(Integer.parseInt(sh.getCell(col+6,row).getContents()),"error");
         	        	  iss.setTracker(tracker);
         	        	 issueManager.update(iss);
         	        	  List<User> urs= usermanager.getUsers();
         	        	  for (User user : urs){
         	        		  if(sh.getCell(col+5, row).getContents().equals(user.getFullName())){
         	        			 
         	        			 iss.setAssignee(user);
             	        	  issueManager.update(iss);
         	        		 
         	        	  }
         	        	  }
         	        	   User ur=usermanager.getCurrentUser();
         	        	   
        	        	   
        	        	    
        				}
        				
        					
        				
        			}
        			
        		}
        	
        		for (int row = 0; row < totalNoOfRows; row++) {

        			for (int col = 0; col < totalNoOfCols; col++) {
        				if(sh.getCell(col, row).getContents().equals("update"))
        				{
        			
        		
        	        int issid=Integer.parseInt(sh.getCell(col+3,row).getContents());
        	        
        	        	Issue iss=issueManager.getIssueById(issid);
     	        	   iss.setStatusId(Integer.parseInt(sh.getCell(col+2,row).getContents()));
     	        	  iss.setPriorityId(Integer.parseInt(sh.getCell(col+4,row).getContents()));
     	        	  
     	        	  issueManager.update(iss); 
    			
     	        	 
     	        	  
     	        	   
    			
        				}
        			}
    	    
			
        	    
        	  
    	     
    	}// end of getJson()
        		return null;
      } 
}
