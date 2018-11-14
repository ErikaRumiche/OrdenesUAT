package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class CommentBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
    private long npCommentId;
    private long npOrderId;
    private String npAction;
    private String npSubject;
    private String npComment;
    private String npCreatedDate;
    private String npCreatedBy;

    public CommentBean() {
    }
    
    public void setNpCommentId(long npCommentId) {
		this.npCommentId = npCommentId;
    }
	
    public long getNpCommentId() {
		return this.npCommentId;
	}

	public void setNpOrderId(long npOrderId) {
		this.npOrderId = npOrderId;
	}
	
    public long getNpOrderId() {
		return this.npOrderId;
	} 

    public void setNpAction(String npAction) {
		this.npAction = npAction;
	}
    
	public String getNpAction() {
		return this.npAction;
	} 
       
    public void setNpSubject(String npSubject) {
		this.npSubject = npSubject;
	}
    
	public String getNpSubject() {
		return this.npSubject;
	}   
       
    public void setNpComment(String npComment) {
		this.npComment = npComment;
	}
	
    public String getNpComment() {
		return this.npComment;
	}        

    public void setNpCreatedDate(String npCreatedDate) {
		this.npCreatedDate = npCreatedDate;
	}
	
    public String getNpCreatedDate() {
		return this.npCreatedDate;
	}
	
    public void setNpCreatedBy(String npCreatedBy) {
		this.npCreatedBy = npCreatedBy;
	}
	
    public String getNpCreatedBy() {
		return this.npCreatedBy;
	}   
}