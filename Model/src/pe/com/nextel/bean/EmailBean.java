package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Timestamp;

import pe.com.nextel.util.GenericObject;


public class EmailBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
	private long lSendMailId;
	private String strSendTo;
	private String strCopyTo;
	private String strBody;
	private String strSubject;
	private String strCreatedBy;
	private Timestamp tsCreatedDate;

	public long getLSendMailId() {
		return lSendMailId;
	}

	public void setLSendMailId(long sendMailId) {
		lSendMailId = sendMailId;
	}

	public String getStrSendTo() {
		return strSendTo;
	}

	public void setStrSendTo(String strSendTo) {
		this.strSendTo = strSendTo;
	}

	public String getStrCopyTo() {
		return strCopyTo;
	}

	public void setStrCopyTo(String strCopyTo) {
		this.strCopyTo = strCopyTo;
	}

	public String getStrBody() {
		return strBody;
	}

	public void setStrBody(String strBody) {
		this.strBody = strBody;
	}

	public String getStrSubject() {
		return strSubject;
	}

	public void setStrSubject(String strSubject) {
		this.strSubject = strSubject;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public Timestamp getTsCreatedDate() {
		return tsCreatedDate;
	}

	public void setTsCreatedDate(Timestamp tsCreatedDate) {
		this.tsCreatedDate = tsCreatedDate;
	}

}