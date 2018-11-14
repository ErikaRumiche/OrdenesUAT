package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * Código: TDECONV003-8
 * <br>Realizado por: PZACARIAS
 * <br>Fecha: 18/06/2018
 */
public class RequestContractFSBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "RequestContractFSBean{" +
                "sn='" + sn + '\'' +
                '}';
    }
}
