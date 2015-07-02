package rushlimit.com.quiqui;

import java.util.UUID;

public class User {

    private UUID mUUID;
    private String mUserId;
    private String mProfilePhoto;
    private String mName;
    private String mEmail;


    public User(String mUserId) {
        this.mUserId = mUserId;
        mUUID = UUID.randomUUID();
    }

    public String getUserId() {
        return mUserId;
    }
}
