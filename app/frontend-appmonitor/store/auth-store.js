import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useAuthStore = defineStore("authStore", () => {
    const token = ref(null);
    const user = ref(null);
    const apiUrl = "http://localhost:8080/appmonitor/api"; // Adjust based on your API's URL
    const isAuthenticated = computed(() => !!token.value);

    // Remove localStorage usage - no longer load from localStorage
    // Simply initialize token and user as null and let them remain in memory during the session

    // Login function
    async function login(userData) {
        try {
            // Attempt login to get the token
            const response = await $fetch(`${apiUrl}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                body: userData,
            });
            if (response) {
                // Save token
                token.value = response;

                // Fetch user details using the token
                const userResponse = await $fetch(`${apiUrl}/auth/user`, {
                    headers: {
                        Accept: "application/json",
                        Authorization: `Bearer ${response}`,
                    },
                });
                if (userResponse) {
                    user.value = userResponse; // Save user info
                }
                return { success: true };
            } else {
                return { success: false, error: "Invalid User response" };
            }
        } catch (error) {
            // Error handling based on the structure of the error object
            console.error("Login failed:", error);
            return { success: false, error: error.message || "Login failed" };
        }
    }

    // Logout function
    function logout() {
        token.value = null;
        user.value = null;
    }


    // Restore state (optional, for future expansions like token refresh)
    // We are no longer using `localStorage`, so no need to restore session here

    return {
        token,
        user,
        isAuthenticated,
        login,
        logout
    };
});
