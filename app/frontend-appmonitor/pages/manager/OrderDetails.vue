<template>
    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
        <h2 class="text-2xl font-semibold mb-4">Order Details</h2>
        <p v-if="loading">Loading...</p>
        <p v-if="error" class="text-red-500">{{ error }}</p>
        <div v-if="order">
            <p><strong>Order ID:</strong> {{ order.id }}</p>
            <p><strong>Customer Name:</strong> {{ order.customerUsername }}</p>
            <p><strong>Created Date:</strong> {{ order.createdDate }}</p>
            <p><strong>Delivered Date:</strong> {{ order.deliveredDate || 'Not Delivered Yet' }}</p>
            <!-- Outros detalhes -->
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRuntimeConfig } from '#imports';

const route = useRoute();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const order = ref(null);
const loading = ref(false);
const error = ref(null);

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

onMounted(() => {
    fetchOrderDetails();
});
</script>
