<template>
    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
        <h2 class="text-2xl font-semibold mb-4">Order Details</h2>
        <p v-if="loading">Loading...</p>
        <p v-if="error" class="text-red-500">{{ error }}</p>
        <div v-if="order">
            <p><strong>Order ID:</strong> {{ order.id }}</p>
            <p><strong>Customer Name:</strong> {{ order.customerUsername }}</p>
            <p><strong>Created Date:</strong> {{ order.createdDate }}</p>
            <p><strong>Delivered Date:</strong> {{ order.deliveredDate || 'Por entregar' }}</p>
            <ul>
            <li v-for="(volume, index) in order.volumes" :key="index">
                <button @click="viewVolumeDetails(volume.id)"  ><p><strong>Volume {{ index + 1 }}:</strong></p></button>
                <p class="ml-4">SentDate: {{ volume.sentDate }}</p>
                <p class="ml-4">Status: {{ volume.deliveredDate ? 'Entregue' : 'Por entregar' }} </p>
                <p class="ml-4">PackageType: {{ volume.packageTypeName }}</p>
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
import { useRuntimeConfig } from '#imports';
import { useRouter } from 'vue-router';

const router = useRouter()

const route = useRoute();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const order = ref(null);
const loading = ref(false);
const error = ref(null);

const viewVolumeDetails = (volumeId) => {
    console.log("Navigating to VolumeDetails with id:", volumeId); 
    router.push({ name: 'volume-id', params: { id: volumeId } }); // Certifique-se de que o nome da rota é 'volume-id'
};


const fetchOrderDetails = async () => {
    loading.value = true;
    error.value = null;
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
    window.history.back(); 
};


onMounted(() => {
    fetchOrderDetails();
});
</script>
