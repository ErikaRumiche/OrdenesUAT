package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by montoymi on 03/02/2016.
 */
public class BagMobileDAO extends GenericDAO {

    public HashMap selectCommunityByPhone(Connection conn, String phoneNumber) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Inicio BagMobileDAO.selectCommunityByPhone");

        String communityName = null;
        String strMessage = null;

        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CELL_BAG.NP_MOBILE_BAG07_PKG.SP_GET_COMMUNITY_BY_PHONE(?, ?, ?); END;";

        try {
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setString(1, phoneNumber);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (StringUtils.isBlank(strMessage)) {
                communityName = cstmt.getString(2);
            }
        } finally {
            closeObjectsDatabase(null, cstmt, null);
        }

        HashMap hshDataMap = new HashMap();
        hshDataMap.put("communityName", communityName);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;
    }

    public HashMap selectCommunitesByCustomerBscsId(Connection conn, long customerBscsId) throws Exception {
        System.out.println("Inicio BagMobileDAO.selectCommunitesByCustomerBscsId: "+customerBscsId);

        List<String> communityNames = new ArrayList<String>();
        String strMessage = null;

        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CELL_BAG.NP_MOBILE_BAG07_PKG.SP_GET_COMMUNITIES_BY_CUSID(?, ?, ?); END;";

        try {
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, customerBscsId);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "CELL_BAG.TT_COMMUNITY");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (StringUtils.isBlank(strMessage)) {
                ARRAY bagMobileArray = (ARRAY) cstmt.getObject(2);
                if(bagMobileArray!=null)
                for (int i = 0; i < bagMobileArray.getOracleArray().length; i++) {
                    STRUCT bagMobileStruct = (STRUCT) bagMobileArray.getOracleArray()[i];

                    String communityName = bagMobileStruct.getOracleAttributes()[0].stringValue();
                    communityNames.add(communityName);
                }
            }
        } finally {
            closeObjectsDatabase(null, cstmt, null);
        }

        HashMap hshDataMap = new HashMap();
        hshDataMap.put("communityNames", communityNames);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;
    }
}
