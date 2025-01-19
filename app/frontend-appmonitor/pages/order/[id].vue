<template>
    <NavBar />
    <div class="max-w-4xl mx-auto mt-8 p-8 bg-white rounded-xl shadow-xl border border-gray-200">

        <!-- Loading and Error States -->
        <p v-if="loading" class="text-center text-xl text-gray-600 font-semibold">⏳ Loading...</p>
        <p v-if="error" class="text-center text-red-500 font-semibold">❌ {{ error }}</p>

        <!-- Order Details Section -->
        <div v-if="order">
            <h2 class="text-3xl font-bold text-gray-800 mb-6 text-center">📦 Order #{{ order.id }}</h2>

            <div class="bg-gray-50 p-5 rounded-lg shadow-sm mb-6 border border-gray-300">
                <p class="text-lg font-medium text-gray-700"><strong>👤 Customer Name:</strong> {{ order.customerUsername }}</p>
                <p class="text-lg font-medium text-gray-700"><strong>📅 Created Date:</strong> {{ new Date(order.createdDate).toLocaleString() }}</p>
                <p class="text-lg font-medium text-gray-700">
                    <strong>🚚 Delivered Date:</strong> 
                    <span class="font-semibold" :class="order.deliveredDate ? 'text-green-600' : 'text-yellow-600'">
                        {{ order.deliveredDate ? new Date(order.deliveredDate).toLocaleString() : ' Por entregar' }}
                    </span>
                </p>
            </div>

            <!-- Volumes Section -->
            <div v-if="order.volumes.length > 0">
                <h3 class="text-2xl font-semibold text-gray-800 mb-4 border-b pb-2">📦 Volumes</h3>
                <ul class="space-y-4">
                    <li v-for="(volume, index) in order.volumes" :key="index" 
                        @click="viewVolumeDetails(volume.id)"
                        class="my-card cursor-pointer bg-gray-50 p-5 rounded-lg shadow-md hover:bg-gray-200 transition border border-gray-300">
                        
                        <span class="text-xl font-bold text-blue-600 my-underline"> Volume {{ index + 1 }}</span>
                        <div class="text-gray-700 flex flex-col gap-2 mt-2">
                            <p><strong>📅 Sent Date:</strong> {{ new Date(volume.sentDate).toLocaleString() }}</p>
                            <p><strong>🚚 Status: </strong>
                                <span class="font-semibold" :class="volume.deliveredDate ? 'text-green-600' : 'text-yellow-600'">
                                    {{ volume.deliveredDate ? new Date(volume.deliveredDate).toLocaleString() : 'Por entregar' }}
                                </span>
                            </p>
                            <p><strong>📦 Package Type:</strong> {{ volume.packageTypeName || 'Não tem' }}</p>
                        </div>
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
import { useRuntimeConfig } from '#imports';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const order = ref(null);
const loading = ref(false);
const error = ref(null);

const viewVolumeDetails = (volumeId) => {
    router.push({ name: 'volume-id', params: { id: volumeId } });
};

const fetchOrderDetails = async () => {
    loading.value = true;
    error.value = null;

    if (isNaN(route.params.id)) {
        error.value = "⚠️ Invalid order ID";
        loading.value = false;
        router.go(-1);
        return;
    }

    try {
        const response = await fetch(`${apiUrl}/orders/${route.params.id}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch order: ${response.statusText}`);
        }
        order.value = await response.json();
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

const goBack = () => {
    router.go(-1);
};

onMounted(() => {
    fetchOrderDetails();
});
</script>

<style scoped>
.my-card:hover .my-underline {
    text-decoration: underline;
}

</style>