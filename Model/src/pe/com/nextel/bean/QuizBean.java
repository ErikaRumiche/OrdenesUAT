package pe.com.nextel.bean;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Admin on 31/07/2015.
 */
public class QuizBean {

    private BigInteger idQuestionary;
    private List<QuestionBean> lstQuestionBean;



    public List<QuestionBean> getLstQuestion() {
        return lstQuestionBean;
    }

    public void setLstQuestion(List<QuestionBean> lstQuestionBean) {
        this.lstQuestionBean = lstQuestionBean;
    }

    public BigInteger getIdQuestionary() {
        return idQuestionary;
    }

    public void setIdQuestionary(BigInteger idQuestionary) {
        this.idQuestionary = idQuestionary;
    }

    @Override
    public String toString() {
        return "QuizBean{" +
                "idQuestionary=" + idQuestionary +
                ", lstQuestionBean=" + lstQuestionBean +
                '}';
    }
}
