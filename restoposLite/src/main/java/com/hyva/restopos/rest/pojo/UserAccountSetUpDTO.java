package com.hyva.restopos.rest.pojo;
import com.hyva.restopos.rest.entities.UserAccessRights;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountSetUpDTO {
    private int useraccount_id;
    private String phone;
    private String user_loginId;
    private String currentPassword;
    private String passwordUser;
    private String full_name;
    private String email;
    private String securityQuestion;
    private String securityAnswer;
    private String status;
    private String orderId;
    private String apimessage;
    private String reTypePassword;
    private boolean employeeflag;
    private String accessLocations;
    private boolean waiterFlag;
    private boolean deliveryFlag;
    private Long userAccessRightsId;
    private UserAccessRights userAccessRights;
    private boolean kitchenflag;
    private Long kitchenId;

    public boolean isKitchenflag() {
        return kitchenflag;
    }

    public void setKitchenflag(boolean kitchenflag) {
        this.kitchenflag = kitchenflag;
    }

    public Long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccessLocations() {
        return accessLocations;
    }

    public void setAccessLocations(String accessLocations) {
        this.accessLocations = accessLocations;
    }

    public boolean isWaiterFlag() {
        return waiterFlag;
    }

    public void setWaiterFlag(boolean waiterFlag) {
        this.waiterFlag = waiterFlag;
    }

    public boolean isDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(boolean deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public boolean isEmployeeflag() {
        return employeeflag;
    }

    public void setEmployeeflag(boolean employeeflag) {
        this.employeeflag = employeeflag;
    }

    public Long getUserAccessRightsId() {
        return userAccessRightsId;
    }

    public void setUserAccessRightsId(Long userAccessRightsId) {
        this.userAccessRightsId = userAccessRightsId;
    }

    public String getApimessage() {
        return apimessage;
    }

    public void setApimessage(String apimessage) {
        this.apimessage = apimessage;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public int getUseraccount_id() {
        return useraccount_id;
    }

    public void setUseraccount_id(int useraccount_id) {
        this.useraccount_id = useraccount_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_loginId() {
        return user_loginId;
    }

    public void setUser_loginId(String user_loginId) {
        this.user_loginId = user_loginId;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
