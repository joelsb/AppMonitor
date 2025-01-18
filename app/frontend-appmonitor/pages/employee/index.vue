<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Card to choose delivery type (Volume or Order) -->
    </div>

    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md gap-6 flex flex-col">
        <!-- Card for Delivery Page -->
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Delivery Page</h2>
            <p class="mb-4 text-lg text-gray-600">Manage the delivery process for your volumes.</p>
            <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="deliverPage">
                Deliver</button>
        </div>

        <!-- Card for New Volume Page -->
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">New Order/Volume Page</h2>
            <p class="mb-4 text-lg text-gray-600">Add new volumes or new orders to your system and track deliveries.</p>
            <button class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600" @click="newVolumePage">
                New Volume/orders</button>
        </div>

        
        
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

// Navigate to the delivery page
const deliverPage = () => {
    router.push('/employee/deliver');
};

// Navigate to the new volume page
const newVolumePage = () => {
    router.push('/employee/newVolume');
};
</script>