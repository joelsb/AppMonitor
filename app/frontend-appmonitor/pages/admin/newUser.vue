<template>
    <NavBar />
    <ProfileForm 
        :userData="userForm" 
        @createProfile="createProfile"
        :editProfile="editProfile" 
        :create="true"
    />
    
    <Popup 
        :show="showPopup" 
        :title="popupTitle" 
        :messages="popupMessages" 
        :type="popupType" 
        @close="closePopup" 
    />
</template>

<script setup>
import { useRuntimeConfig } from '#imports';
import { ref } from 'vue';
import NavBar from '~/components/NavBar.vue';
import ProfileForm from '~/components/ProfileForm.vue';
import Popup from '~/components/Popup.vue';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');

const editProfile = ref(true); // Always in "edit mode" for creating a user
const userForm = ref({ 
    username: '',
    name: '',
    email: '',
    password: '',
    role: '',
    warehouse: '',
    office: '',
});

const createProfile = async (formValue) => {
    popupMessages.value = [];
    try {
        let userRole = formValue.role.toLowerCase() + 's';
        //exclude warehouse if not an employee
        if (userRole !== 'employees') {
            delete formValue.warehouse;
        }
        //exclude office if not a manager
        if (userRole !== 'managers') {
            delete formValue.office;
        }
        const response = await fetch(`${apiUrl}/${userRole}`, {  // POST to /users
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formValue),
        });

        const data = response.ok ? await response.json() : await response.text();
        if (!response.ok) {
            showPopup.value = true;
            popupTitle.value = 'Error';
            popupMessages.value.push(data || 'Failed to create user.');
            popupType.value = 'failure';
        } else {
            showPopup.value = true;
            popupTitle.value = 'Success';
            popupMessages.value.push('User created successfully!');
            popupType.value = 'success';
        }
    } catch (error) {
        showPopup.value = true;
        popupTitle.value = 'Error';
        popupMessages.value.push('An error occurred while creating the user.');
        popupType.value = 'error';
    }
};

const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
};
</script>

