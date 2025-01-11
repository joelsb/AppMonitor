<script setup>
import { useAuthStore } from "~/store/auth-store.js";

const router = useRouter()
const authStore = useAuthStore()
~
function logout() {
    authStore.logout()
    router.push('/')
}
</script>

<template>
    <div>
        <slot />
    </div>
</template>