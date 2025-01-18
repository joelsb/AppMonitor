<template>
    <!-- NavBar -->
    <NavBar />

    <!-- Package Type Table Section -->
    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
        <h2 class="text-2xl font-semibold mb-4">Package Types</h2>
        <div class="flex-row justify-between flex items-center">
            <span class="text-lg text-gray-600">View all package types available in the app.</span>
            <button @click="router.go(-1)" class="px-6 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
                Voltar
            </button>
        </div>

        <!-- Package Types Table -->
        <div v-if="packageTypes.length === 0" class="text-center text-gray-500">
            No package types available.
        </div>
        <table v-if="packageTypes.length > 0" class="table w-full mt-4">
            <thead>
                <tr>
                    <th class="p-3 font-semibold text-left">ID</th>
                    <th class="p-3 font-semibold text-left">Name</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="packageType in packageTypes" :key="packageType.id">
                    <td class="p-3"><button @click="viewPackageTypeDetails(packageType.id)"
                            class="text-blue-600 hover:underline">
                            {{ packageType.id }}
                        </button></td>
                    <td class="p-3">{{ packageType.name }}</td>
                </tr>
                <!-- NEW PACKAGE TYPE IN A ROW -->
                <tr>
                    <td class="p-3 flex-row justify-end" colspan="2">
                        <button @click="router.push('/package-type/create')"
                            class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 w-full">
                            Create a new Package Type
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <!--  -->
    </div>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import NavBar from '@/components/NavBar.vue';
import { useRuntimeConfig } from '#imports';

const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Reactive Data
const packageTypes = ref([]);

// Fetch Package Types
const fetchPackageTypes = async () => {
    try {
        const response = await fetch(`${apiUrl}/package-types`);
        if (!response.ok) {
            throw new Error(`Failed to fetch package types: ${response.statusText}`);
        }
        packageTypes.value = await response.json();
    } catch (err) {
    }
};

// View Package Type Details
const viewPackageTypeDetails = (id) => {
    router.push({ name: 'package-type-id', params: { id: id } });
};

// Fetch Data on Mount
onMounted(() => {
    fetchPackageTypes();
});
// Go back
const goBack = () => {
    window.history.back();
};
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

.table tr:nth-child(even) {
    background-color: #f9f9f9;
}

.table tr:hover {
    background-color: #f1f1f1;
}
</style>
