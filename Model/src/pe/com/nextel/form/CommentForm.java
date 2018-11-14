package pe.com.nextel.form;

public class CommentForm extends GenericForm {
	
	private String hdnOrderId;
	private String hdnFlagSave;
	private String txtSubject;
	private String txtDescription;
	private String hdnLogin;
	private String hdnUserId;
        private String hdnAppId;

	public void setHdnOrderId(String hdnOrderId) {
		this.hdnOrderId = hdnOrderId;
	}

	public String getHdnOrderId() {
		return hdnOrderId;
	}

	public void setHdnFlagSave(String hdnFlagSave) {
		this.hdnFlagSave = hdnFlagSave;
	}

	public String getHdnFlagSave() {
		return hdnFlagSave;
	}

	public void setTxtSubject(String txtSubject) {
		this.txtSubject = txtSubject;
	}

	public String getTxtSubject() {
		return txtSubject;
	}

	public void setTxtDescription(String txtDescription) {
		this.txtDescription = txtDescription;
	}

	public String getTxtDescription() {
		return txtDescription;
	}

	public void setHdnLogin(String hdnLogin) {
		this.hdnLogin = hdnLogin;
	}

	public String getHdnLogin() {
		return hdnLogin;
	}

	public void setHdnUserId(String hdnUserId) {
		this.hdnUserId = hdnUserId;
	}

	public String getHdnUserId() {
		return hdnUserId;
	}

        public void setHdnAppId(String hdnAppId) {
            this.hdnAppId = hdnAppId;
        }
    
        public String getHdnAppId() {
            return hdnAppId;
        }
}
