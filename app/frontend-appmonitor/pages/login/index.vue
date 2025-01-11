<template>
    <div class="login-container">
        <div class="login-card">
            <h2 class="login-title">Login</h2>
            <form @submit.prevent="login">
                <!-- Username Field -->
                <div class="form-group">
                    <label for="username">Username</label>
                    <div class="input-group">
                        <span class="icon">
                            <i class="fas fa-user"></i> <!-- FontAwesome user icon -->
                        </span>
                        <input class="form-control" id="username" v-model="loginFormData.username"
                            placeholder="Enter your username" />
                        <!-- Error Message for Username -->

                    </div>
                </div>

                <!-- Password Field -->
                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="input-group">
                        <span class="icon">
                            <i class="fas fa-lock"></i> <!-- FontAwesome lock icon -->
                        </span>
                        <input type="password" class="form-control" id="password" v-model="loginFormData.password"
                            placeholder="Enter your password" />
                        <!-- Error Message for Password -->
                    </div>
                </div>
                
                <!-- se a password ou username tiverem incorretos -->
                <span v-if="messages.length > 0" class="error-text ">
                    Invalid username or password.
                </span>
                <!-- Submit Button -->
                <button type="submit" class="btn btn-primary" :disabled="!isFormValid" @click="login">
                    Login
                </button>
            </form>

        </div>
        <div v-if="token">
            <h3>Token:</h3>
            <div>{{ token }}</div>
        </div>
        <div v-if="user">
            <h3>User Info:</h3>
            <pre>{{ user }}</pre>
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
const { token } = storeToRefs(authStore);

// Form Fields
const loginFormData = reactive({
    username: "",
    password: "",
});

const messages = ref([]);
const isFormValid = computed(() => loginFormData.username.trim() && loginFormData.password.trim());

// Login method
async function login() {
    messages.value = []; // Limpar mensagens anteriores
    try {
        await $fetch(`${api}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
            body: loginFormData,
            onResponse({ response }) {
                if (response.status === 200) {
                    token.value = response._data; // Salva o token na store
                    router.push("/homepage"); // Redireciona para a página inicial
                } else {
                    messages.value.push({
                        status: response.status,
                        payload: response._data,
                    });
                }
            },
        });
    } catch (e) {
        console.error("Login falhou:", e);
    }
}


</script>


<style scoped>
.error-text {
    color: #e74c3c;
    /* Red for error */
    font-size: 0.875rem;
    /* Smaller font size */
    margin-top: 0.5rem;
    display: block;
    text-align: center;
    /* Center the text */
    width: 100%;
    /* Make sure the error text spans the full width */
}

.error-text i {
    margin-right: 0.5rem;
}

.btn {
    background-color: #007bff;
    color: #ffffff;
    border: none;
    padding: 0.75rem 1.5rem;
    /* Increase padding for a bigger button */
    font-size: 1.25rem;
    /* Larger font size */
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
    width: 100%;
    /* Full width button */
    max-width: 300px;
    /* Optional: Limit maximum width */
    margin: 1rem auto 0;
    /* Center button and add spacing */
    display: block;
    /* Ensure the button is centered */
}

.btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
    pointer-events: none;
}

.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f5f5f5;
}

.login-card {
    background: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    padding: 2rem;
    width: 100%;
    max-width: 400px;
    text-align: center;
}

.login-title {
    font-size: 1.5rem;
    margin-bottom: 1.5rem;
    color: #333333;
}

.form-group {
    margin-bottom: 1.5rem;
    text-align: left;
}

.input-group {
    display: flex;
    align-items: center;
    border: 1px solid #ced4da;
    border-radius: 4px;
    padding: 0.5rem;
    background: #f9f9f9;
}

.input-group .icon {
    margin-right: 0.5rem;
    color: #6c757d;
}

.input-group .form-control {
    border: none;
    outline: none;
    background: none;
    flex: 1;
}

.btn:hover {
    background-color: #0056b3;
}
</style>