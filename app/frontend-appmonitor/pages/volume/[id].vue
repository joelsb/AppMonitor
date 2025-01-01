<template>
    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
        <h2 class="text-2xl font-semibold mb-4">Volume Details</h2>
        <p v-if="loading">Loading...</p>
        <p v-if="error" class="text-red-500">{{ error }}</p>
        <div v-if="volume">
            <p><strong>Volume {{ volume.id }}:</strong></p>
            <p class="ml-4"><strong>Sent Date:</strong> {{ volume.sentDate }}</p>
            <p class="ml-4">
                <strong>Status:</strong> {{ volume.deliveredDate ? 'Entregue' : 'Por entregar' }}
            </p>
            <p class="ml-4"><strong>Package Type:</strong> {{ volume.packageTypeName }}</p>
            <ul>
            <li v-for="(product, index) in volume.products" :key="index">
                <p><strong>Product {{ index + 1 }}:</strong></p>
                <button  
                                    @click="viewVolumeDetails(product.id)" 
                                    class="text-dark-600 hover:underline ml-4">
                                    {{ product.id }}
                </button>
                <p class="ml-4">SentDate: {{ product.name }}</p>
                </li>
            </ul>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRuntimeConfig } from '#imports';

// Obter parâmetros da rota e configuração de API
const route = useRoute();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Definição de variáveis reativas
const volume = ref(null);
const loading = ref(false);
const error = ref(null);

// Função para buscar detalhes do volume
const fetchVolumeDetails = async () => {
    const volumeId = route.params.id; // Obter o ID do volume da rota
    if (!volumeId) {
        error.value = "Volume ID not found in route parameters.";
        return;
    }

    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/volumes/${volumeId}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch volume: ${response.statusText}`);
        }
        const data = await response.json();
        // Supondo que o backend retorna um array
        volume.value = Array.isArray(data) ? data[0] : data;
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

// Buscar os detalhes do volume ao montar o componente
onMounted(() => {
    fetchVolumeDetails();
});
</script>
