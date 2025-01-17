<template>
    <div v-if="loading" class="text-center py-6">
        <p>Loading...</p>
    </div>

    <div v-if="error" class="text-center py-6 text-red-500">
        <p>{{ error }}</p>
    </div>

    <div v-if="volume">
        <div class="max-w-4xl mx-auto mt-6 p-6 bg-white rounded-lg shadow-lg">
            <h2 class="text-2xl font-semibold mb-4 text-gray-800">Volume {{ volume.id }} Details</h2>

            <!-- Informações principais do volume -->
            <div class="space-y-4">
                <p class="text-gray-700"><strong>Sent Date:</strong> {{ volume.sentDate }}</p>
                <p class="text-gray-700"><strong>Status:</strong> 
                    <span class="font-semibold {{ volume.deliveredDate ? 'text-green-500' : 'text-yellow-500' }}">
                        {{ volume.deliveredDate ? 'Entregue' : 'Por entregar' }}
                    </span>
                </p>
                <p class="text-gray-700"><strong>Package Type:</strong> {{ volume.packageTypeName || 'Não há necessidade' }}</p>
            </div>

            <!-- Lista de Produtos -->
            <div class="mt-4">
                <h3 class="text-xl font-semibold text-gray-800 mb-3">Products</h3>
                <ul class="space-y-3">
                    <li v-for="(product, index) in volume.products" :key="index" class="border-b pb-3">
                        <p class="text-gray-700"><strong>Product:</strong> {{ product.productName }}</p>
                        <p class="ml-4 text-gray-600">Quantidade: {{ product.quantity }}</p>
                    </li>
                </ul>
            </div>

            <!-- Lista de Sensores -->
            <div class="mt-6">
                <h3 class="text-xl font-semibold text-gray-800 mb-3">Sensors</h3>
                <ul class="space-y-4">
                    <li v-for="(sensor, index) in volume.sensors" :key="index">
                        <button 
                            @click="viewSensorDetails(sensor.id)" 
                            class="text-blue-500 font-semibold hover:underline">
                            <p><strong>Sensor {{ index + 1 }}:</strong></p>
                        </button>
                        <p class="ml-6 text-gray-700">Type: {{ sensor.sensorType.name }}</p>
                        
                        <div v-if="sensor.history && sensor.history.length > 0" class="mt-2">
                            <h4 class="text-lg font-semibold text-gray-700">Sensor History</h4>
                            <ul class="space-y-2">
                                <li v-for="(history, index) in sensor.history" :key="index" class="border-t pt-2">
                                    <p class="text-gray-600"><strong>Time:</strong> {{ history.time }}</p>
                                    <p class="text-gray-600"><strong>Value:</strong> {{ history.value }}</p>
                                </li>
                            </ul>
                        </div>
                        <div v-else class="mt-2 text-gray-500">Sem histórico disponível.</div>
                    </li>
                </ul>
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

const viewSensorDetails = (sensorId) => {
    console.log("Navigating to Sensor Detail with id:", sensorId); 
    router.push({ name: 'sensor-id', params: { id: sensorId } }); // Certifique-se de que o nome da rota é 'sensor-id'
};

// Buscar os detalhes do volume ao montar o componente
onMounted(() => {
    fetchVolumeDetails();
});
</script>
