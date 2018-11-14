package pe.com.nextel.bean;

import java.math.BigInteger;

/**
 * Created by HHuaraca on 31/07/2015.
 */
public class OptionBean {

    private BigInteger idoption;
    private String option;



    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public BigInteger getIdoption() {
        return idoption;
    }

    public void setIdoption(BigInteger idoption) {
        this.idoption = idoption;
    }
}
