<template>
        <!-- NavBar -->
        <NavBar />

        <!-- Show PackageTypeForm for Creating or Editing -->
      <PackageTypeForm :isCreating="true" :isEditing="true"/>
      
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import NavBar from '@/components/NavBar.vue';
import PackageTypeForm from '@/components/PackageTypeForm.vue';
import { useRuntimeConfig } from '#imports';

const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Reactive Data
const packageTypes = ref([]);

// Go back
const goBack = () => {
    window.history.back();
};
</script>

<style scoped>
.table-container {
    overflow-x: auto;
}

.table {
    border-collapse: collapse;
    width: 100%;
}

.table th,
.table td {
    border: 1px solid #ccc;
    padding: 0.75rem;
}

.table th {
    background-color: #f9f9f9;
}

.table td {
    text-align: left;
}
</style>
