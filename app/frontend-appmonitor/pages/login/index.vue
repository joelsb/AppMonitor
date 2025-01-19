<template>
    <div class="login-container">
        <div class="login-card">
            <h2 class="login-title">Login 👋</h2>
            <form @submit.prevent="login" class="login-form">
                <!-- Username Field -->
                <div class="form-group">
                    <!-- <label for="username">Username</label> -->
                    <div class="input-group">
                        <span class="icon">
                            <i class="fas fa-user"></i> <!-- FontAwesome user icon -->
                        </span>
                        <input class="form-control" id="username" v-model="loginFormData.username"
                            placeholder="Enter your username" />
                    </div>
                </div>

                <!-- Password Field -->
                <div class="form-group">
                    <!-- <label for="password">Password</label> -->
                    <div class="input-group">
                        <span class="icon">
                            <i class="fas fa-lock"></i> <!-- FontAwesome lock icon -->
                        </span>
                        <input type="password" class="form-control" id="password" v-model="loginFormData.password"
                            placeholder="Enter your password" />
                    </div>
                </div>
                
                <!-- Error Message for Invalid Login -->
                <span v-if="messages.length > 0" class="error-text">
                    ❌ Invalid username or password.
                </span>
                
                <!-- Submit Button -->
                <button type="submit" class="btn" :disabled="!isFormValid">
                    Login 🔑
                </button>
            </form>
        </div>
    </div>
</template>

<script setup>

import { storeToRefs } from "pinia"
import { useAuthStore } from "~/store/auth-store.js"
import { reactive, ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const config = useRuntimeConfig();
const api = config.public.API_URL;
const router = useRouter();

const authStore = useAuthStore();

// Form Fields
const loginFormData = reactive({
    username: "Admin",
    password: "123",
});

const messages = ref([]);
const isFormValid = computed(() => loginFormData.username.trim() && loginFormData.password.trim());

// Login method
async function login() {
    messages.value = [];

    // Call the store's login method with the loginFormData object
    const response = await authStore.login(loginFormData);
    if (response.success) {
        // Redirect to the homepage
        router.push("/homepage");
    } else {
        messages.value.push(response.error);
    }
}


</script>


<style scoped>
.login-form {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}

.error-text {
    color: #e74c3c;
    font-size: 0.875rem;
    display: block;
    text-align: center;
    width: 100%;
}

.error-text i {
    margin-right: 0.5rem;
}

.btn {
    background-color: #007bff;
    color: #ffffff;
    border: none;
    padding: 0.75rem 1.5rem;
    font-size: 1.25rem;
    border-radius: 50px;
    cursor: pointer;
    transition: background-color 0.3s, transform 0.2s ease;
    width: 80%;
    margin-top: 20px;
}

.btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}

.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f0f4f8;
}

.login-card {
    display: flex;
    flex-direction: column;
    gap: 20px;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
    padding: 3rem;
    width: 100%;
    max-width: 400px;
    text-align: center;
}

.login-title {
    text-align: center;
    font-size: 2.5rem;
    margin: 0;
    color: #333333;
    font-weight: 600;
    margin-bottom: 20px;
}

.form-group {
    width: 100%;
    margin: 0;
    text-align: left;
}

.input-group {
    display: flex;
    align-items: center;
    border: 1px solid #ced4da;
    border-radius: 8px;
    padding: 0.75rem;
    background: #f9f9f9;
    transition: all 0.3s ease;
}

.input-group .icon {
    margin-right: 1rem;
    color: #6c757d;
}

.input-group .form-control {
    border: none;
    outline: none;
    background: none;
    flex: 1;
    font-size: 1rem;
    padding: 0.75rem;
}

.input-group:hover {
    border-color: #007bff;
    box-shadow: 0 0 10px rgba(0, 123, 255, 0.2);
}

.btn:hover {
    background-color: #0056b3;
    transform: scale(1.05);
}
</style>