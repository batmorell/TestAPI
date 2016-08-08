package org.api.bo;


import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.api.enumeration.TokenTypeEnum;
import org.api.utils.Utils;

public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    private int mId;
    private int mIdUser;
    private String mValue;
    private TokenTypeEnum mType;
    private Date mTsCreation;
    private Date mTsUpdate;
    private Date mTsExpiration;

    public int getId() {
        return mId;
    }

    public void setId(int pId) {
        mId = pId;
    }

    public int getIdUser() {
        return mIdUser;
    }

    public void setIdUser(int pIdUser) {
        mIdUser = pIdUser;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String pValue) {
        mValue = pValue;
    }

    public TokenTypeEnum getType() {
        return mType;
    }

    public void setType(TokenTypeEnum pType) {
        mType = pType;
    }

    public Date getTsCreation() {
        return mTsCreation;
    }

    public void setTsCreation(Date pTsCreation) {
        mTsCreation = pTsCreation;
    }

    public Date getTsUpdate() {
        return mTsUpdate;
    }

    public void setTsUpdate(Date pTsUpdate) {
        mTsUpdate = pTsUpdate;
    }

    public Date getTsExpiration() {
        return mTsExpiration;
    }

    public void setTsExpiration(Date pTsExpiration) {
        mTsExpiration = pTsExpiration;
    }

    public boolean hasExpired() {
        return new Date().getTime() > mTsExpiration.getTime();
    }

    public static Token mapIn(ResultSet pResultSet) throws SQLException {
        Token lToken = new Token();
        lToken.setId(pResultSet.getInt("id"));
        lToken.setIdUser(pResultSet.getInt("id_user"));
        lToken.setValue(pResultSet.getString("value"));
        lToken.setType(TokenTypeEnum.fromString(pResultSet.getString("type")));
        lToken.setTsCreation(Utils.convertTimestampToDate(pResultSet.getTimestamp("ts_creation")));
        lToken.setTsUpdate(Utils.convertTimestampToDate(pResultSet.getTimestamp("ts_update")));
        lToken.setTsExpiration(Utils.convertTimestampToDate(pResultSet.getTimestamp("ts_expiration")));

        return lToken;
    }

}
