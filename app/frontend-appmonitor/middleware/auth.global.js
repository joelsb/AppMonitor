// middleware/auth.global.js
import { useAuthStore } from "~/store/auth-store.js"

export default defineNuxtRouteMiddleware((to, from) => {
    const authStore = useAuthStore();
    if (!authStore.token && to.path !== '/login') {
        return navigateTo('/login');
    }
});