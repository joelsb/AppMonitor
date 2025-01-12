// src/utils/fetch.js
import { useAuthStore } from "~/store/auth-store.js"

// const apiUrl = 'http://localhost:8080/appmonitor/api'; // Adjust as needed

export default async function fetchWithAuth(url, options = {}) {
    const authStore = useAuthStore();  // Get the auth store
    const token = authStore.token;     // Access the token directly from the store

    // Set default headers if not provided
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,  // Add Authorization header
        ...options.headers,  // Merge any custom headers passed in options
    };
    try {
        // Make the API request with the combined headers
        const response = await fetch(`${url}`, { ...options, headers });

        // Parse and return the response as JSON
        return response;
    } catch (error) {
        console.error('Error in fetchWithAuth:', error);
        return error;  // Return the error to be handled by the calling component
    }
}