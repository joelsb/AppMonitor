<template>
    <NavBar />
    
    <div v-if="error" class="text-center py-6 text-red-500">
        <p>{{ error }}</p>
    </div>

    <ProfileForm 
        v-if="userForm.username" 
        :userData="userForm" 
        @updateProfile="updateProfile"
        :editProfile="editProfile"
    />

    <Popup 
        :show="showPopup" 
        :title="popupTitle" 
        :messages="popupMessages" 
        :type="popupType" 
        @close="closePopup" 
    />

    <div class="mt-6 text-center">
        <button @click="goBack" class="px-6 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
            Voltar
        </button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRuntimeConfig } from '#imports';
import NavBar from '~/components/NavBar.vue';
import ProfileForm from '~/components/ProfileForm.vue';
import Popup from '~/components/Popup.vue';

// Setup API
const route = useRoute();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// State variables
const userForm = ref({});
const loading = ref(false);
const error = ref(null);
const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');
const editProfile = ref(false);

// Fetch user data
const fetchUserDetails = async () => {
    loading.value = true;
    error.value = null;
    const username = route.params.username;

    if (!username) {
        error.value = "User ID not found in route parameters.";
        loading.value = false;
        return;
    }

    try {
        // Get user role dynamically
        const authResponse = await fetch(`${apiUrl}/users/${username}`);
        const authUser = await authResponse.json();
        const userRole = authUser.role.toLowerCase() + "s";

        // Fetch user details
        const response = await fetch(`${apiUrl}/${userRole}/${username}`);
        if (!response.ok) throw new Error(`Failed to fetch user: ${response.statusText}`);
        
        userForm.value = await response.json();
        userForm.value.role = authUser.role;
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

// Update profile function
const updateProfile = async (formValue) => {
    editProfile.value = true;
    popupMessages.value = [];
    
    try {
        // Get user role dynamically
        const authResponse = await fetch(`${apiUrl}/users/${formValue.username}`);
        const authUser = await authResponse.json();
        const userRole = authUser.role.toLowerCase() + "s";

        const response = await fetch(`${apiUrl}/${userRole}/${formValue.username}`, {
            method: 'PUT',
            body: JSON.stringify(formValue),
        });

        const data = response.ok ? await response.json() : await response.text();
        if (!response.ok) {
            showPopup.value = true;
            popupTitle.value = 'Error';
            popupMessages.value.push(data.message || 'Failed to update profile.');
            popupType.value = 'error';
            editProfile.value = true;
        } else {
            showPopup.value = true;
            popupTitle.value = 'Success';
            popupMessages.value.push('Profile updated successfully!');
            popupType.value = 'success';
            editProfile.value = false;
        }
    } catch (error) {
        showPopup.value = true;
        popupTitle.value = 'Error';
        popupMessages.value.push('An error occurred during the update.');
        popupType.value = 'error';
    }
};

// Close popup
const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
    window.history.back();

};

// Go back
const goBack = () => {
    window.history.back();
};

// Fetch user details on mount
onMounted(fetchUserDetails);
</script>
