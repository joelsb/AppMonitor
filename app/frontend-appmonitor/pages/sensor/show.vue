<template>
    <div>
        <!-- NavBar -->
        <NavBar />

        <!-- sensor Table Section -->
        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Sensor Page</h2>
            <p class="mb-4 text-lg text-gray-600">See all the sensors.</p>

            <!-- Loading Indicator -->
            <div v-if="loading" class="flex justify-center items-center">
                <svg class="animate-spin h-8 w-8 text-gray-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
                </svg>
            </div>

            <!-- Error Message -->
            <div v-if="error" class="text-red-500 text-center mb-4">{{ error }}</div>

            <!-- sensors Table -->
            <div v-if="!loading && !error" class="table-container">
                <div v-if="sensors.length === 0" class="text-center text-gray-500">
                    No sensors yet
                </div>
                <table v-if="sensors.length > 0" aria-label="sensors table" class="table w-full">
                    <thead>
                        <tr>
                            <th class="p-3 font-semibold text-left">Id</th>
                            <th class="p-3 font-semibold text-left">Type</th>
                            <th class="p-3 font-semibold text-left">Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="sensor in paginatedSensors" :key="sensor.id">
                            <td class="p-3">
                                <button 
                                    @click="viewsensorDetails(sensor.id)" 
                                    class="text-blue-600 hover:underline">
                                    {{ sensor.id }}
                                </button>
                            </td>
                            <td class="p-3">{{ sensor.sensorType.name }}</td>
                            <td class="p-3">{{ sensor.history && sensor.history.length > 0 ? sensor.history[0].value : 'Sem valor disponível'}}</td>
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
import { useRouter } from 'vue-router'; // Import useRouter apenas uma vez

const router = useRouter(); // Mantenha apenas uma declaração de 'router'

// API Config
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Reactive Data
const sensors = ref([]);
const loading = ref(false);
const error = ref(null);
const currentPage = ref(1);
const pageSize = 10;

// Computed Paginated sensors
const paginatedSensors = computed(() => 
    sensors.value.slice((currentPage.value - 1) * pageSize, currentPage.value * pageSize)
);

// Total Pages
const totalPages = computed(() => Math.ceil(sensors.value.length / pageSize));

// Função para ver os detalhes do pedido e redirecionar
const viewsensorDetails = (id) => {
    router.push({ name: 'sensor-id', params: { id: id } });
};



// Fetch sensors Function
const fetchSensors = async () => {
    loading.value = true;
    error.value = null;
    try {
        const response = await fetch(`${apiUrl}/sensors`);
        if (!response.ok) {
            throw new Error(`Failed to fetch: ${response.statusText}`);
        }
        sensors.value = await response.json();
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
    fetchSensors();
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
