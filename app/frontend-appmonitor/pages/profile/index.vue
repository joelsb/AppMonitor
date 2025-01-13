<template>
    <NavBar />
    <!-- Create a card for the profile page -->
    <div class="card">
        <div class="card-header">
            <h2>Profile</h2>
        </div>
        <div class="card-body">
            <form>
                <!-- Role -->
                <div class="form-group">
                    <label for="role">Role</label>
                    <input
                        type="text"
                        id="role"
                        v-model="userForm.role"
                        class="form-control"
                        disabled
                    />
                </div>

                <!-- Username -->
                <div class="form-group">
                    <label for="username">Username</label>
                    <input
                        type="text"
                        id="username"
                        v-model="userForm.username"
                        class="form-control"
                        disabled
                    />
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label for="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        v-model="userForm.email"
                        class="form-control"
                        disabled
                    />
                </div>

                <!-- Name -->
                <div class="form-group">
                    <label for="name">Name</label>
                    <input
                        type="text"
                        id="name"
                        v-model="userForm.name"
                        class="form-control"
                        disabled
                    />
                </div>

                <!-- Office for Manager -->
                <div
                    v-if="userForm.role === 'Manager'"
                    class="form-group"
                >
                    <label for="office">Office</label>
                    <input
                        type="text"
                        id="office"
                        v-model="userForm.office"
                        class="form-control"
                        disabled
                    />
                </div>

                <!-- Warehouse for Employee -->
                <div
                    v-if="userForm.role === 'Employee'"
                    class="form-group"
                >
                    <label for="warehouse">Warehouse</label>
                    <input
                        type="text"
                        id="warehouse"
                        v-model="userForm.warehouse"
                        class="form-control"
                        disabled
                    />
                </div>

                <!-- Messages -->
                <div v-if="messages.length" class="form-messages">
                    <ul>
                        <li v-for="(message, index) in messages" :key="index" class="message-item">
                            {{ message }}
                        </li>
                    </ul>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { useAuthStore } from '~/store/auth-store';
import { useRuntimeConfig } from '#imports';
import { ref, onMounted } from 'vue';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const user = useAuthStore().user;

// Make userRole lowercase and add an 's' to the end
const userRole = user.role.toLowerCase() + 's';

const userForm = ref({
    role: user.role,
    username: user.username,
    email: user.email,
    name: user.name,
    password: '',
    confirmPassword: '',
    office: null,
    warehouse: null,
});

const messages = ref([]);

onMounted(async () => {
    try {
        const response = await fetch(`${apiUrl}/${userRole}/${user.username}`);
        const data = response.ok ? await response.json() : await response.text();

        // Check if the data is a string or array
        if (typeof data === 'string') {
            messages.value.push(data);
        } else if (Array.isArray(data)) {
            messages.value.push(...data);
        } else if (response.status === 401) {
            console.error('Unauthorized access');
            return;
        }
        // Assign data to userForm
        userForm.value = {
            ...userForm.value,
            ...data,
        };
    } catch (error) {
        console.error(`Error fetching ${userRole}/${user.username}:`, error);
    }
});
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
</style>
