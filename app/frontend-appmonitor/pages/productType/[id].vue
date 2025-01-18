<template>
    <div v-if="loading" class="text-center py-6">
        <p>Loading...</p>
    </div>

    <div v-if="error" class="text-center py-6 text-red-500">
        <p>{{ error }}</p>
    </div>

    <div v-if="productType">
        <div class="max-w-4xl mx-auto mt-6 p-6 bg-white rounded-lg shadow-lg">
            <h2 class="text-2xl font-semibold mb-4 text-gray-800">Product {{ productType.id }} Details</h2>

            <!-- Informações principais do productType -->
            <div class="space-y-4">
                <p class="text-gray-700"><strong>Name:</strong> {{ productType.name }}</p>
                <p class="text-gray-700"><strong>Mandatory Package:</strong> {{ productType.mandatoryPackage}}  </p>
                <p class="text-gray-700"><strong>Mandatory Sensors</strong></p>
                <ul v-if="productType.mandatorySensors && productType.mandatorySensors.length">
                <li v-for="(sensor, index) in productType.mandatorySensors" :key="index">
                     - {{ sensor.name }}
                </li>
                </ul>
                <p v-else>
                    Não há necessidade
                </p>
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
const productType = ref(null);
const loading = ref(false);
const error = ref(null);

// Função para buscar detalhes do productType
const fetchVolumeDetails = async () => {
    const productTypeId = route.params.id; // Obter o ID do productType da rota
    if (!productTypeId) {
        error.value = "Volume ID not found in route parameters.";
        return;
    }

    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/product-types/${productTypeId}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch productType: ${response.statusText}`);
        }
        const data = await response.json();
        // Supondo que o backend retorna um array
        productType.value = Array.isArray(data) ? data[0] : data;
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

// Buscar os detalhes do productType ao montar o componente
onMounted(() => {
    fetchVolumeDetails();
});
</script>
