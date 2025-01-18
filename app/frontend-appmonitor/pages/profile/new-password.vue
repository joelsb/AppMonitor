<template>
    <NavBar />
    <div class="card">
        <div class="card-header">
            <div class="mb-4 flex-row justify-between flex items-center">
            <h2>Change Password</h2>

                <button @click="router.go(-1)" class="px-6 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
                Voltar
            </button>
            </div>
        </div>
        <div class="card-body">
            <form @submit.prevent="handleChangePassword">
                <!-- Current Password -->
                <div class="form-group">
                    <label for="currentPassword">Current Password</label>
                    <input type="password" id="currentPassword" v-model="passwordForm.currentPassword"
                        class="form-control" placeholder="Enter your current password" required />
                </div>

                <!-- New Password -->
                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" v-model="passwordForm.newPassword" class="form-control"
                        placeholder="Enter your new password" required />
                </div>

                <!-- Confirm New Password -->
                <div class="form-group">
                    <label for="newPasswordConfirmation">Confirm New Password</label>
                    <input type="password" id="newPasswordConfirmation" v-model="passwordForm.newPasswordConfirmation"
                        class="form-control" placeholder="Confirm your new password" required />
                </div>
                <!-- Error Messages -->
                <div v-if="errorMessage" class="form-error">
                    {{ errorMessage }}
                </div>
                <!-- Submit Button -->
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Change Password
                    </button>
                </div>
            </form>
        </div>
    </div>
    <!-- Success, Failure or Information Popup -->
    <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '~/store/auth-store';
import Popup from '@/components/Popup.vue'; // Import the Popup component

const router = useRouter();
// PopUp state
const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info'); // Can be 'success' or 'error'

const passwordForm = ref({
    currentPassword: '',
    newPassword: '',
    newPasswordConfirmation: '',
});

const errorMessage = ref('');

const handleChangePassword = async () => {
    // Reset the error message
    popupMessages.value = [];
    if (passwordForm.value.newPassword !== passwordForm.value.newPasswordConfirmation) {
        errorMessage.value = "New passwords don't match!";
        return;
    }

    try {
        const authStore = useAuthStore();
        const response = await authStore.changePassword(passwordForm.value);

        if (response.success) {
            // Clear the form fields and reset the error message
            passwordForm.value.currentPassword = '';
            passwordForm.value.newPassword = '';
            passwordForm.value.newPasswordConfirmation = '';
            popupTitle.value = 'Success!';
            popupType.value = 'success';
            popupMessages.value.push("Password changed successfully!");
            errorMessage.value = '';
            showPopup.value = true;
        } else {
            // Set the error message returned by the API or fallback to a default
            popupTitle.value = 'Error!';
            popupMessages.value.push(response.error || 'An unexpected error occurred.');
            popupType.value = 'failure';
            showPopup.value = true;
        }
    } catch (error) {
        errorMessage.value = "An unexpected error occurred.";
    }
};

// Close the popup
const closePopup = () => {
    showPopup.value = false;
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
    margin: 0.75rem;
    text-align: center;
}
</style>
