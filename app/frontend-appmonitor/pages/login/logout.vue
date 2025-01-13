<template>
  <!-- Optionally, you can add a loading spinner or message here -->
  <div>Logging out...</div>
</template>

<script setup>
import { useAuthStore } from '~/store/auth-store';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Automatically log out when the page is accessed
onMounted(() => {
  useAuthStore().logout();
  router.push('/login');
});

</script>

<style>
/* Optional styles */
</style>
