package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * Created by montoymi on 03/02/2016.
 */
public class BagMobileBean implements Serializable {
    private String communityName;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
