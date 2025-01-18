<template>
    <div>
        <!-- NavBar -->
        <NavBar />

        <!-- User Orders Table -->
        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Orders for {{ username }}</h2>
            <p class="mb-4 text-lg text-gray-600">All orders for this user are listed below.</p>

            <!-- Loading Indicator -->
            <div v-if="loading" class="flex justify-center items-center">
                <svg class="animate-spin h-8 w-8 text-gray-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
                </svg>
            </div>

            <!-- Error Message -->
            <div v-if="error" class="text-red-500 text-center mb-4">{{ error }}</div>

            <!-- Orders Table -->
            <div v-if="!loading && !error" class="table-container">
                <table aria-label="User Orders table" class="table w-full">
                    <thead>
                        <tr>
                            <th class="p-3 font-semibold text-left">Order ID</th>
                            <th class="p-3 font-semibold text-left">Order Created Date</th>
                            <th class="p-3 font-semibold text-left">Status</th>
                            <th class="p-3 font-semibold text-left">Count of Volumes</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="order in userOrders" :key="order.id">
                            <td class="p-3">
                                <button 
                                    @click="viewOrderDetails(order.id)" 
                                    class="text-blue-600 hover:underline">
                                    {{ order.id }}
                                </button>
                            </td>
                            <td class="p-3">{{ new Date(order.createdDate).toLocaleString() }}</td>
                            <td class="p-3">
                                {{ order.deliveredDate ? 'Entregue' : 'Por entregar' }}
                            </td>
                            <td class="p-3">{{ order.volumes.length }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useRuntimeConfig } from '#imports';

import NavBar from '@/components/NavBar.vue';

const route = useRoute();
const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Reactive Data
const username = route.params.username;
const userOrders = ref([]);
const loading = ref(false);
const error = ref(null);

// Fetch User Orders
const fetchUserOrders = async () => {
    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/customers/${username}/orders`);
        if (!response.ok) {
            throw new Error(`Failed to fetch orders for ${username}: ${response.statusText}`);
        }
        userOrders.value = await response.json();
    } catch (err) {
        error.value = err.message;
    } finally {
        loading.value = false;
    }
};

// View Order Details
const viewOrderDetails = (orderId) => {
    router.push({ name: 'order-id', params: { id: orderId } });
};

// Fetch Data on Mount
onMounted(() => {
    fetchUserOrders();
});
</script>

<style scoped>
.table-container {
    overflow-x: auto;
}

.table {
    border-collapse: collapse;
    width: 100%;
}

.table th,
.table td {
    border: 1px solid #ccc;
    padding: 0.75rem;
}

.table th {
    background-color: #f9f9f9;
}

.table td {
    text-align: left;
}
</style>
