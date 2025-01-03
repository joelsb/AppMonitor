<template>
    
        <p v-if="loading">Loading...</p>
        <p v-if="error" class="text-red-500">{{ error }}</p>
        <div v-if="volume">
            <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Volume {{ volume.id }} Details</h2>
            <p class="ml"><strong>Sent Date:</strong> {{ volume.sentDate }}</p>
            <p class="ml">
                <strong>Status:</strong> {{ volume.deliveredDate ? 'Entregue' : 'Por entregar' }}
            </p>
            <p class="ml"><strong>Package Type:</strong> {{ volume.packageTypeName }}</p>
            <ul>
            <li v-for="(product, index) in volume.products" :key="index">
                <p class="ml-4"><strong>Product {{ product.productId }}:</strong></p>
                <p class="ml-8">Quantidade: {{ product.quantity }}</p>
            </li>
            </ul>
            <ul>
            <li v-for="(sensor, index) in volume.sensors" :key="index">
                <p class="ml-4"><strong>Sensor {{ sensor.id }}:</strong></p>
                <p class="ml-8">Type:  {{ sensor.sensorType.name }}</p>
                <ul>
                <li v-for="(history, index) in sensor.history" :key="index">
                    <p class="ml-4"><strong>History Sensor {{ sensor.id }}:</strong></p>
                    <p class="ml-8">Time: {{ history.time }}</p>
                    <p class="ml-8">Value: {{ history.value }}</p>
                </li>
                </ul>
            </li>
            </ul>
            <button 
                    @click="goBack"
                    class="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition">
                    Voltar
            </button>
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

// Função para voltar à página anterior
const goBack = () => {
    window.history.back(); 
};

// Buscar os detalhes do volume ao montar o componente
onMounted(() => {
    fetchVolumeDetails();
});
</script>

