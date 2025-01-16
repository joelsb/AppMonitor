<template>
    <NavBar />
    <div class="card">
        <div class="card-header">
            <h2>Edit Profile</h2>
        </div>
        <div class="card-body">
            <form @submit.prevent="updateProfile">
                <div class="form-group">
                    <label for="role">Role</label>
                    <input type="text" id="role" v-model="user.role" class="form-control" disabled />
                </div>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" v-model="editForm.username" class="form-control" disabled />
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" v-model="editForm.email" class="form-control" />
                </div>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" v-model="editForm.name" class="form-control" />
                </div>
                <div v-if="editForm.role === 'Manager'" class="form-group">
                    <label for="office">Office</label>
                    <input type="text" id="office" v-model="editForm.office" class="form-control" />
                </div>
                <div v-if="editForm.role === 'Employee'" class="form-group">
                    <label for="warehouse">Warehouse</label>
                    <input type="text" id="warehouse" v-model="editForm.warehouse" class="form-control" />
                </div>
                <div v-if="editForm.office" class="form-group">
                    <label for="office">Office</label>
                    <input type="text" id="office" v-model="editForm.office" class="form-control" />
                </div>
                <div v-if="editForm.warehouse" class="form-group">
                    <label for="warehouse">Warehouse</label>
                    <input type="text" id="warehouse" v-model="editForm.warehouse" class="form-control" />
                </div>
                <div v-if="messages.length" class="form-messages">
                    <ul>
                        <li v-for="(message, index) in messages" :key="index" class="message-item">{{ message }}</li>
                    </ul>
                </div>
                <div class="edit-button">
                    <button
                        class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">Edit Profile</button>
                </div>
            </form>
        </div>
    </div>
    <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />
</template>

<script setup>
import { ref } from 'vue';
import { useRuntimeConfig } from '#imports';
import Popup from '~/components/Popup.vue';
import { useAuthStore } from '~/store/auth-store';


//get the userName from the route
const route = useRoute();
const username = route.params.username;

const user = useAuthStore().user;

// Make userRole lowercase and add an 's' to the end
const rolePath = user.role.toLowerCase() + 's';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const messages = ref([]);
const editForm = ref({})


onMounted(async () => {
    try {
        const response = await fetch(`${apiUrl}/${rolePath}/${username}`);
        editForm.value = await response.json();
    } catch (error) {
        messages.value.push('Failed to fetch user data.');
    }
});


const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');

const updateProfile = async () => {
    messages.value = [];
    try {
        const response = await fetch(`${apiUrl}/${rolePath}/${username}`, {
            method: 'PUT',
            body: JSON.stringify(editForm.value),
        });
        const data = response.ok ? await response.json() : await response.text();
        if (!response.ok) {
            showPopup.value = true;
            popupTitle.value = 'Error';
            popupMessages.value.push(data.message || 'Failed to update profile.');
            popupType.value = 'error';
        } else {
            showPopup.value = true;
            popupTitle.value = 'Success';
            popupMessages.value.push('Profile updated successfully!');
            popupType.value = 'success';
        }
    } catch (error) {
        showPopup.value = true;
        popupTitle.value = 'Success';
        popupMessages.value.push('Profile updated successfully!');
        popupType.value = 'success';
    }
};

function closePopup() {
    showPopup.value = false;
}
</script>

<style scoped>
.edit-button {
    display: flex;
    justify-content: flex-end;
}
.card {
    max-width: 600px;
    margin: 2rem auto;
    padding: 1.5rem;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.card-header h2 {
    text-align: center;
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

.btn-primary {
    display: block;
    width: 100%;
    padding: 0.75rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 0.375rem;
    cursor: pointer;
}

.form-messages ul {
    color: red;
    list-style: none;
    padding: 0;
    margin-top: 1rem;
}
</style>
