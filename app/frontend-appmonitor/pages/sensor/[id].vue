<template>
    <p v-if="loading">Loading...</p>
    <p v-if="error" class="text-red-500">{{ error }}</p>
    <div v-if="sensor">
        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Sensor com id {{ sensor.id }} Details</h2>
            <p class="ml"><strong>Sensor Type:</strong> {{ sensor.sensorType.name }}</p>
            <p class="ml"><strong>Unit:</strong> {{ sensor.sensorType.unit }}</p>

            <!-- Exibe o valor mais recente do histórico ou uma mensagem caso não haja histórico -->
            <p class="ml"><strong>Value:</strong> 
                {{ sensor.history && sensor.history.length > 0 ? sensor.history[0].value : 'Sem valor disponível' }}
            </p>

            <h3 class="text-xl font-semibold mt-4 mb-2">Sensor History</h3>
            <div v-if="sensor.history && sensor.history.length > 0">
                <!-- Tabela para exibir o histórico -->
                <table class="min-w-full table-auto border-collapse border border-gray-300">
                    <thead>
                        <tr class="bg-gray-100">
                            <th class="border border-gray-300 px-4 py-2 text-left">Time</th>
                            <th class="border border-gray-300 px-4 py-2 text-left">Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(history, index) in sensor.history" :key="index">
                            <td class="border border-gray-300 px-4 py-2">{{ history.time }}</td>
                            <td class="border border-gray-300 px-4 py-2">{{ history.value }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div v-else>
                <p class="ml-4">Sem histórico disponível.</p>
            </div>

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
const sensor = ref(null);
const loading = ref(false);
const error = ref(null);

// Função para buscar detalhes do sensor
const fetchSensorDetails = async () => {
    const sensorId = route.params.id; // Obter o ID do sensor da rota
    if (!sensorId) {
        error.value = "Sensor ID not found in route parameters.";
        return;
    }

    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/sensors/${sensorId}`); // A URL deve corresponder à sua API
        if (!response.ok) {
            throw new Error(`Failed to fetch sensor: ${response.statusText}`);
        }
        const data = await response.json();
        sensor.value = data; // Presumindo que o backend retorna um objeto de sensor

        // Ordenar o histórico pelo tempo mais recente
        if (sensor.value.history && sensor.value.history.length > 0) {
            sensor.value.history.sort((a, b) => new Date(b.time) - new Date(a.time));
        }
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

// Buscar os detalhes do sensor ao montar o componente
onMounted(() => {
    fetchSensorDetails();
});
</script>
