<template>
    <NavBar />
    <div class="max-w-4xl mx-auto mt-8 p-8 bg-white rounded-xl shadow-xl border border-gray-200">
        <!-- Loading and Error States -->
        <p v-if="loading" class="text-center text-xl text-gray-600 font-semibold">⏳ Loading...</p>
        <p v-if="error" class="text-center text-red-500 font-semibold">❌ {{ error }}</p>

        <!-- Sensor Details -->
        <div v-if="sensor">
            <h2 class="text-3xl font-bold text-gray-800 mb-6 text-center">
                📡 Sensor #{{ sensor.id }} - Details
            </h2>

            <div class="bg-gray-50 p-6 rounded-lg shadow-md border border-gray-300">
                <p class="text-lg font-medium text-gray-700"><strong>🛠 Sensor Type:</strong> {{ sensor.sensorType.name }}</p>
                <p class="text-lg font-medium text-gray-700">
                    <strong>📊 Latest Value: </strong> 
                    <span class="font-semibold text-blue-600">
                        {{ sensor.history && sensor.history.length > 0 ? sensor.history[0].value + sensor.sensorType.unit : 'Sem valor disponível' }}
                    </span>
                </p>
            </div>

            <!-- Sensor History -->
            <h3 class="text-2xl font-semibold text-gray-800 mt-8 mb-4 border-b pb-2">📜 Sensor History</h3>
            <div v-if="sensor.history && sensor.history.length > 0">
                <div class="overflow-x-auto">
                    <table class="w-full table-auto border-collapse shadow-lg">
                        <thead>
                            <tr class="bg-blue-500 text-white text-left">
                                <th class="border border-blue-600 px-4 py-3">⏰ Time</th>
                                <th class="border border-blue-600 px-4 py-3">📊 Value</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(history, index) in sensor.history" :key="index" 
                                class="hover:bg-gray-100 transition">
                                <td class="border border-gray-300 px-4 py-2">{{ history.time }}</td>
                                <td class="border border-gray-300 px-4 py-2 font-semibold text-blue-600">
                                    {{ history.value }}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div v-else>
                <p class="text-gray-600 text-lg">⚠️ Sem histórico disponível.</p>
            </div>

            <!-- Back Button -->
            <div class="mt-8 text-center">
                <button 
                    @click="goBack"
                    class="w-full py-3 bg-blue-500 text-white font-semibold text-lg rounded-full hover:bg-blue-600 transition shadow-md">
                    🔙 Back
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRuntimeConfig } from '#imports';
import NavBar from '~/components/NavBar.vue';

const route = useRoute();
const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const sensor = ref(null);
const loading = ref(false);
const error = ref(null);

const fetchSensorDetails = async () => {
    const sensorId = route.params.id;
    if (!sensorId) {
        error.value = "⚠️ Sensor ID not found.";
        return;
    }
    loading.value = true;
    error.value = null;

    try {
        const response = await fetch(`${apiUrl}/sensors/${sensorId}`);
        const data = response.ok ? await response.json() : await response.text();
        if (!response.ok) {
            error.value = `Failed to fetch sensor details: ${data}`;
        }
        sensor.value = data;

        if (sensor.value.history && sensor.value.history.length > 0) {
            sensor.value.history.sort((a, b) => new Date(b.time) - new Date(a.time));
        }
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

const goBack = () => {
    window.history.back();
};

onMounted(() => {
    fetchSensorDetails();
});
</script>
