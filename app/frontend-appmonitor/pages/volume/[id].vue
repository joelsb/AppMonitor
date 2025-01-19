<template>
    <NavBar />
    <div v-if="loading" class="text-center py-6 text-gray-600 text-lg font-semibold">
        <p>Loading...</p>
    </div>

    <div v-if="error" class="text-center py-6 text-red-500 font-semibold">
        <p>{{ error }}</p>
    </div>

    <div v-if="volume">
        <div class="max-w-4xl mx-auto mt-8 p-8 bg-white rounded-xl shadow-xl border border-gray-200">
            <h2 class="text-3xl font-bold mb-6 text-gray-800 text-center">📦 Volume #{{ volume.id }} Details</h2>

            <!-- Volume Info -->
            <div class="space-y-4 text-lg">
                <p class="text-gray-700"><strong>📅 Sent Date:</strong> {{ volume.sentDate }}</p>
                <p class="text-gray-700"><strong>🚚 Status: </strong> 
                    <span :class="volume.deliveredDate ? 'text-green-600' : 'text-yellow-600'">
                        {{ volume.deliveredDate ? 'Entregue' : 'Por entregar' }}
                    </span>
                </p>
                <p class="text-gray-700"><strong>📦 Package Type:</strong> {{ volume.packageTypeName || 'Não há necessidade' }}</p>
            </div>

            <!-- Products List -->
            <div class="mt-6">
                <h3 class="text-2xl font-semibold text-gray-800 mb-4 border-b pb-2">🛒 Products</h3>
                <ul class="space-y-4">
                    <li v-for="(product, index) in volume.products" :key="index" class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                        <p class="text-gray-800 font-medium"><strong>📌 Product:</strong> {{ product.productName }}</p>
                        <p class="ml-4 text-gray-600">🛒 Quantidade: {{ product.quantity }}</p>
                    </li>
                </ul>
            </div>

            <!-- Sensors List -->
            <div class="mt-8">
                <h3 class="text-2xl font-semibold text-gray-800 mb-4 border-b pb-2">📟 Sensors</h3>
                <ul class="space-y-6">
                    <li v-for="(sensor, index) in volume.sensors" :key="index" class="my-card cursor-pointer bg-gray-50 p-5 rounded-lg shadow-md hover:bg-gray-200 transition border border-gray-300" 
                    @click="viewSensorDetails(sensor.id)" >
                        <span 
                            class="text-blue-500 font-semibold text-lg my-underline">
                            <p> Sensor {{ index + 1 }}</p>
                        </span>
                        <p class="ml-6 text-gray-700">🔍 Type: {{ sensor.sensorType.name }}</p>
                        
                        <div v-if="sensor.history && sensor.history.length > 0" class="mt-3">
                            <h4 class="text-lg font-semibold text-gray-700">📜 Sensor History</h4>
                            <ul class="space-y-2">
                                <li v-for="(history, index) in sensor.history" :key="index" class="border-t pt-2 text-gray-600">
                                    <p><strong>🕒 Time:</strong> {{ history.time }}</p>
                                    <p><strong>📊 Value:</strong> {{ history.value }}</p>
                                </li>
                            </ul>
                        </div>
                        <div v-else class="mt-2 text-gray-500">🚫 Sem histórico disponível.</div>
                    </li>
                </ul>
            </div>

            <!-- Back Button -->
            <div class="mt-8 text-center">
                <button @click="goBack"
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

// Route & API config
const route = useRoute();
const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Reactive state
const volume = ref(null);
const loading = ref(false);
const error = ref(null);

// Fetch Volume Details
const fetchVolumeDetails = async () => {
    const volumeId = route.params.id;
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
        volume.value = Array.isArray(data) ? data[0] : data;
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

// Navigation Functions
const goBack = () => {
    window.history.back(); 
};

const viewSensorDetails = (sensorId) => {
    router.push({ name: 'sensor-id', params: { id: sensorId } });
};

// Fetch volume details on mount
onMounted(() => {
    fetchVolumeDetails();
});
</script>

<style scoped>
.my-card:hover .my-underline {
    text-decoration: underline;
}
</style>
