package org.api.bo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.api.enumeration.AccessLevelEnum;
import org.json.JSONException;

import org.api.utils.CustomJSONObject;
import org.api.utils.JSONAble;
import org.api.utils.TechniqueException;
import org.api.utils.Utils;

public class User implements Serializable, JSONAble {

    private static final long serialVersionUID = 1L;

    protected int mId;
    protected String mEmail;
    protected String mFirstName;
    protected String mLastName;
    protected String mProfile;
    protected String mCompany;
    protected String mPassword;
    protected boolean mActive;
    protected Date mTsCreation;
    protected Date mTsUpdate;
    protected AccessLevelEnum mAccessLevel;

    public User() {
        super();
    }

    public User(User pUser) {
        this.mId = pUser.getId();
        this.mEmail = pUser.getEmail();
        this.mLastName = pUser.getLastName();
        this.mFirstName = pUser.getFirstName();
        this.mPassword = pUser.getPassword();
        this.mActive = pUser.isActive();
        this.mTsCreation = pUser.getTsCreation();
        this.mTsUpdate = pUser.getTsUpdate();
        this.mAccessLevel = pUser.getAccessLevel();
    }

    public int getId() {
        return mId;
    }

    public void setId(int pId) {
        mId = pId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String pEmail) {
        mEmail = pEmail;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String pLastName) {
        mLastName = pLastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String pFirstName) {
        mFirstName = pFirstName;
    }

    public String getFullName() {
        if (mFirstName != null && mLastName != null) {
            return mFirstName + " " + mLastName;
        }
        return null;
    }

    public String getProfile() {
        return mProfile;
    }

    public void setProfile(String pProfile) {
        mProfile = pProfile;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String pCompany) {
        mCompany = pCompany;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String pPassword) {
        mPassword = pPassword;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean pActive) {
        mActive = pActive;
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

    public AccessLevelEnum getAccessLevel() {
        return mAccessLevel;
    }

    public void setAccessLevel(AccessLevelEnum pAccessLevel) {
        mAccessLevel = pAccessLevel;
    }

    public static User mapIn(ResultSet pResultSet) throws TechniqueException, SQLException {
        return mapIn(pResultSet, "");
    }

    public static User mapInForApplicant(ResultSet pResultSet) throws TechniqueException, SQLException {
        return mapIn(pResultSet, "_app");
    }

    public static User mapInForRecruiter(ResultSet pResultSet) throws TechniqueException, SQLException {
        return mapIn(pResultSet, "_rec");
    }

    public static User mapInForApplicanTemp(ResultSet pResultSet) throws TechniqueException, SQLException {
        return mapIn(pResultSet, "_appt");
    }

    public static User mapIn(ResultSet pResultSet, String pSufix) throws TechniqueException, SQLException {
        User lUser = new User();
        lUser.setId(pResultSet.getInt("id"));
        lUser.setEmail(pResultSet.getString("email"));
        lUser.setLastName(pResultSet.getString("last_name"));
        lUser.setFirstName(pResultSet.getString("first_name"));
        lUser.setProfile(pResultSet.getString("profil"));
        lUser.setCompany(pResultSet.getString("company"));
        lUser.setActive(pResultSet.getBoolean("active"));
        lUser.setTsUpdate(Utils.convertTimestampToDate(pResultSet.getTimestamp("ts_update")));
        lUser.setTsCreation(Utils.convertTimestampToDate(pResultSet.getTimestamp("ts_creation")));
        lUser.setAccessLevel(AccessLevelEnum.fromString(pResultSet.getString("access_level")));
        return lUser;
    }

    public CustomJSONObject toJSON() throws TechniqueException {
        CustomJSONObject lJSONUser = new CustomJSONObject();
        try {
            lJSONUser.put("id", mId);
            lJSONUser.put("email", mEmail);
            lJSONUser.put("firstName", mFirstName);
            lJSONUser.put("lastName", mLastName);
            lJSONUser.put("profile", mProfile);
            lJSONUser.put("company", mCompany);
            lJSONUser.put("password", mPassword);
            lJSONUser.put("active", mActive);
            lJSONUser.put("tsCreation", mTsCreation);
            lJSONUser.put("tsUpdate", mTsUpdate);
            lJSONUser.put("accessLevel", mAccessLevel);
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new TechniqueException();
        }
        return lJSONUser;
    }

    public static User mapInWithPassword(ResultSet pResultSet) throws TechniqueException, SQLException {
        User lUser = mapIn(pResultSet);
        lUser.setPassword(pResultSet.getString("password"));
        return lUser;
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(getId());
    }
}
