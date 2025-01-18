<template>
    <NavBar />
    <ProfileForm v-if="userForm.username" :userData="userForm" @updateProfile="updateProfile"
        :editProfile="editProfile" :create="false"/>
    <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />
    <!-- TODO: FAlta rever o editProfile sent to the profileFrom -->
    <!-- TODO: falta rever a logic de update profile, nao funciona -->
</template>

<script setup>
import { useAuthStore } from '~/store/auth-store';
import { useRuntimeConfig } from '#imports';
import { ref, onMounted } from 'vue';
import NavBar from '~/components/NavBar.vue';
import ProfileForm from '~/components/ProfileForm.vue';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const user = useAuthStore().user;
const userRole = user.role.toLowerCase() + 's';

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');

const userForm = ref({});

const messages = ref([]);

const editProfile = ref(false)

onMounted(async () => {
    try {
        const response = await fetch(`${apiUrl}/${userRole}/${user.username}`);
        userForm.value = await response.json();
        userForm.value.role = user.role;
    } catch (error) {
        console.error(`Error fetching ${userRole}/${user.username}:`, error);
    }
});

const updateProfile = async (formValue) => {
    editProfile.value = true;
    popupMessages.value = [];
    try {
        const response = await fetch(`${apiUrl}/${userRole}/${user.username}`, {
            method: 'PUT',
            body: JSON.stringify(formValue),  // Use formValue here
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

const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
};

</script>

<style scoped></style>
