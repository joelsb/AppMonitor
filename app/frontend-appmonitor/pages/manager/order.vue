<template>
    <div>
        <!-- NavBar -->
        <NavBar />

        <!-- Order Table Section -->
        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Order Page</h2>
            <p class="mb-4 text-lg text-gray-600">See all the orders.</p>

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
                <table aria-label="Orders table" class="table w-full">
                    <thead>
                        <tr>
                            <th class="p-3 font-semibold text-left">Order ID</th>
                            <th class="p-3 font-semibold text-left">Customer Name</th>
                            <th class="p-3 font-semibold text-left">Order Created Date</th>
                            <th class="p-3 font-semibold text-left">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="order in paginatedOrders" :key="order.id">
                            <td class="p-3">
                                <button 
                                    @click="goToOrder(order.id)" 
                                    class="text-blue-600 hover:underline">
                                    {{ order.id }}
                                </button>
                            </td>
                            <td class="p-3">{{ order.customerUsername }}</td>
                            <td class="p-3">{{ order.createdDate }}</td>
                            <td class="p-3">
                                {{ order.deliveredDate ? 'Entregue' : 'Por entregar' }}
                            </td>

                        </tr>
                    </tbody>
                </table>

                <!-- Pagination -->
                <div class="flex justify-between items-center mt-4">
                    <button 
                        @click="prevPage" 
                        :disabled="currentPage === 1" 
                        class="px-3 py-1 bg-gray-200 rounded disabled:opacity-50">
                        Previous
                    </button>
                    <span>Page {{ currentPage }}</span>
                    <button 
                        @click="nextPage" 
                        :disabled="currentPage === totalPages" 
                        class="px-3 py-1 bg-gray-200 rounded disabled:opacity-50">
                        Next
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRuntimeConfig } from '#imports';
import NavBar from '@/components/NavBar.vue'; // Import NavBar component

import { useRouter } from 'vue-router';

const router = useRouter();

// API Config
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Reactive Data
const orders = ref([]);
const loading = ref(false);
const error = ref(null);
const currentPage = ref(1);
const pageSize = 10;

// Computed Paginated Orders
const paginatedOrders = computed(() => 
    orders.value.slice((currentPage.value - 1) * pageSize, currentPage.value * pageSize)
);

// Total Pages
const totalPages = computed(() => Math.ceil(orders.value.length / pageSize));


// Navigate to a specific order page by ID
const goToOrder = (orderId) => router.push(`/order/${orderId}`);


// Fetch Orders Function
const fetchOrders = async () => {
    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/orders`);
        if (!response.ok) {
            throw new Error(`Failed to fetch: ${response.statusText}`);
        }
        orders.value = await response.json();
    } catch (err) {
        error.value = err.message;
        console.error(err);
    } finally {
        loading.value = false;
    }
};

// Pagination Functions
const nextPage = () => {
    if (currentPage.value < totalPages.value) {
        currentPage.value++;
    }
};

const prevPage = () => {
    if (currentPage.value > 1) {
        currentPage.value--;
    }
};

// Fetch Data on Mount
onMounted(() => {
    fetchOrders();
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
