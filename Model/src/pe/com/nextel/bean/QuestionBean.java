package pe.com.nextel.bean;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by HHuaraca on 31/07/2015.
 */
public class QuestionBean {

    private BigInteger idquestion;
    private String question;
    private BigInteger idoptionSuccess;
    private List<OptionBean> lstOptionBean;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<OptionBean> getLstOption() {
        return lstOptionBean;
    }

    public void setLstOption(List<OptionBean> lstOptionBean) {
        this.lstOptionBean = lstOptionBean;
    }

    public BigInteger getIdquestion() {
        return idquestion;
    }

    public void setIdquestion(BigInteger idquestion) {
        this.idquestion = idquestion;
    }

    public BigInteger getIdoptionSuccess() {
        return idoptionSuccess;
    }

    public void setIdoptionSuccess(BigInteger idoptionSuccess) {
        this.idoptionSuccess = idoptionSuccess;
    }
}
