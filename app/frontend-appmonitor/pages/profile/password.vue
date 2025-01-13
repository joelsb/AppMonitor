<template>
    <NavBar />
        <div class="card">
            <div class="card-header">
                <h2>Change Password</h2>
            </div>
            <div class="card-body">
                <form @submit.prevent="handleChangePassword">
                    <!-- Current Password -->
                    <div class="form-group">
                        <label for="currentPassword">Current Password</label>
                        <input
                            type="password"
                            id="currentPassword"
                            v-model="passwordForm.currentPassword"
                            class="form-control"
                            placeholder="Enter your current password"
                            required
                        />
                    </div>

                    <!-- New Password -->
                    <div class="form-group">
                        <label for="newPassword">New Password</label>
                        <input
                            type="password"
                            id="newPassword"
                            v-model="passwordForm.newPassword"
                            class="form-control"
                            placeholder="Enter your new password"
                            required
                        />
                    </div>

                    <!-- Confirm New Password -->
                    <div class="form-group">
                        <label for="confirmNewPassword">Confirm New Password</label>
                        <input
                            type="password"
                            id="confirmNewPassword"
                            v-model="passwordForm.confirmNewPassword"
                            class="form-control"
                            placeholder="Confirm your new password"
                            required
                        />
                    </div>

                    <!-- Submit Button -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            Change Password
                        </button>
                    </div>

                    <!-- Error Messages -->
                    <div v-if="errorMessage" class="form-error">
                        {{ errorMessage }}
                    </div>
                </form>
            </div>
        </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '~/store/auth-store';

const passwordForm = ref({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: '',
});

const errorMessage = ref('');

const handleChangePassword = async () => {
    if (passwordForm.value.newPassword !== passwordForm.value.confirmNewPassword) {
        errorMessage.value = "New passwords don't match!";
        return;
    }
    try {
        const authStore = useAuthStore();
        await authStore.changePassword(passwordForm.value);
        // Optionally, clear the form fields
        passwordForm.value.currentPassword = '';
        passwordForm.value.newPassword = '';
        passwordForm.value.confirmNewPassword = '';
    } catch (error) {
        console.error('Failed to change password:', error);
        errorMessage.value = 'An error occurred while changing the password.';
    }
};
</script>

<style scoped>
.card {
    max-width: 600px;
    margin: 2rem auto;
    padding: 1.5rem;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.card-header h2 {
    margin: 0;
    font-size: 1.5rem;
    text-align: center;
}

.card-body {
    margin-top: 1rem;
}

.form-group {
    margin-bottom: 1rem;
}

.form-control {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 0.375rem;
    font-size: 1rem;
}

.form-messages ul {
    margin: 1rem 0 0;
    padding: 0;
    list-style: none;
    color: red;
}

.form-messages li {
    font-size: 0.875rem;
}
.change-password-page {
    max-width: 400px;
    margin: 2rem auto;
    padding: 1.5rem;
    background-color: #ffffff;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    border-radius: 0.5rem;
}

.card-header {
    text-align: center;
    margin-bottom: 1rem;
}

.card-header h2 {
    font-size: 1.5rem;
    margin: 0;
}

.form-group {
    margin-bottom: 1rem;
}

.form-control {
    width: 100%;
    padding: 0.5rem;
    font-size: 1rem;
    border: 1px solid #ddd;
    border-radius: 0.375rem;
}

.form-actions {
    text-align: center;
}

.btn-primary {
    padding: 0.5rem 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 0.375rem;
    cursor: pointer;
}

.btn-primary:hover {
    background-color: #0056b3;
}

.form-error {
    color: red;
    font-size: 0.875rem;
    margin-top: 0.5rem;
    text-align: center;
}
</style>
