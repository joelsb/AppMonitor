<template>
    <div class="card">
        <!-- <div class="card-header">
            <h2>{{ isEditing ? "Edit Profile" : "Profile" }}</h2>
        </div> -->
        <div class="card-body">
            <!-- Edit Mode Toggle Switch -->
            <div class="flex justify-end items-center">
                <label for="editProfileSwitch" class="block font-semibold text-lg mr-3">Edit Mode</label>
                <label for="editProfileSwitch" class="inline-flex relative items-center cursor-pointer">
                    <input type="checkbox" id="editProfileSwitch" v-model="isEditing" class="sr-only" />
                    <div class="w-14 h-6 rounded-full transition-colors"
                        :class="isEditing ? 'bg-blue-500' : 'bg-gray-200'"></div>
                    <div class="absolute left-1 top-1 w-4 h-4 bg-white rounded-full transition-transform"
                        :class="isEditing ? 'transform translate-x-7' : ''"></div>
                </label>
                <span class="ml-4 text-lg">{{ isEditing ? 'Editing' : 'View' }}</span>
            </div>
            <form class="form" @submit.prevent="updateProfile">
                <!-- Role -->
                <div class="form-group">
                    <label for="role">Role</label>
                    <input type="text" id="role" v-model="userRole" class="form-control" disabled />
                </div>

                <!-- Username -->
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" v-model="form.username" class="form-control" disabled />
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" v-model="form.email" class="form-control" :disabled="!isEditing" />
                </div>

                <!-- Name -->
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" v-model="form.name" class="form-control" :disabled="!isEditing" />
                </div>

                <!-- Office for Manager -->
                <div v-if="userRole === 'Manager'" class="form-group">
                    <label for="office">Office</label>
                    <input type="text" id="office" v-model="form.office" class="form-control" :disabled="!isEditing" />
                </div>

                <!-- Warehouse for Employee -->
                <div v-if="userRole === 'Employee'" class="form-group">
                    <label for="warehouse">Warehouse</label>
                    <input type="text" id="warehouse" v-model="form.warehouse" class="form-control"
                        :disabled="!isEditing" />
                </div>

                <!-- Messages -->
                <div v-if="messages.length" class="form-messages">
                    <ul>
                        <li v-for="(message, index) in messages" :key="index" class="message-item">
                            {{ message }}
                        </li>
                    </ul>
                </div>



                <!-- Save Button (Only in Edit Mode) -->
                <div v-if="isEditing" class="edit-button">
                    <button type="submit" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">
                        Save Changes
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '~/store/auth-store';

const props = defineProps({
    userData: Object, // Initial data
    editProfile: Boolean
});

const userRole = useAuthStore().user.role;

const isEditing = ref(false);

const emit = defineEmits(["updateProfile"]);

const router = useRouter();
const user = useAuthStore().user;
const messages = ref([]);

const form = ref({ ...props.userData });

watch(() => props.userData, (newData) => {
    form.value = { ...newData };
}, { deep: true });

watch(() => props.editProfile, (newEditProfile) => {
    isEditing.value = newEditProfile;
});

function updateProfile() {
    emit("updateProfile", form.value);
}

</script>

<style scoped>
.form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}
.card-body {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}
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

.form-control {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 0.375rem;
    font-size: 1rem;
}

.form-messages ul {
    color: red;
    list-style: none;
    padding: 0;
    margin-top: 1rem;
}
</style>
