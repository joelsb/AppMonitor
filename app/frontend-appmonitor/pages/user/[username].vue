<template>
    <div v-if="loading" class="text-center py-6">
        <p>Loading...</p>
    </div>

    <div v-if="error" class="text-center py-6 text-red-500">
        <p>{{ error }}</p>
    </div>

    <div v-if="user">
        <div class="max-w-4xl mx-auto mt-6 p-6 bg-white rounded-lg shadow-lg">
            <h2 class="text-2xl font-semibold mb-4 text-gray-800">User {{ user.username }} Details</h2>

            <!-- Informações principais do user -->
            <div class="space-y-4">
                <p class="text-gray-700"><strong>Name:</strong> {{ user.name }}</p>
                <p class="text-gray-700"><strong>Mail:</strong> {{ user.email }}     </p>
                <p class="text-gray-700"><strong>Role:</strong> {{ user.role }}</p>
            </div>

            <!-- Botão Voltar -->
            <div class="mt-6 text-center">
                <button 
                    @click="goBack"
                    class="px-6 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
                    Voltar
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRuntimeConfig } from '#imports';

// Obter parâmetros da rota, configuração de API e roteador
const route = useRoute();
const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Definição de variáveis reativas
const user = ref(null);
const loading = ref(false);
const error = ref(null);

// Função para buscar detalhes do user
const fetchUserDetails = async () => {
    const username = route.params.username; // Obter o ID do user da rota
    if (!username) {
        error.value = "User ID not found in route parameters.";
        return;
    }

    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/users/${username}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch user: ${response.statusText}`);
        }
        const data = await response.json();
        // Supondo que o backend retorna um array
        user.value = Array.isArray(data) ? data[0] : data;
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

// Função para voltar à página anterior
const goBack = () => {
    window.history.back(); 
};

// Buscar os detalhes do user ao montar o componente
onMounted(() => {
    fetchUserDetails();
});
</script>
