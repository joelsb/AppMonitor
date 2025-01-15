package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewPasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirmation;

    public NewPasswordDTO() {
    }

    public NewPasswordDTO(String currentPassword, String newPassword, String newPasswordConfirmation) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
