<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Card to choose delivery type (Volume or Order) -->
        <div class="max-w-4xl mx-auto mt-6 p-8 bg-white rounded-lg shadow-lg space-y-6 flex flex-col">
            
            <!-- Order Page Section -->
            <div class="bg-gradient-to-r from-blue-500 to-indigo-600 p-6 rounded-lg shadow-lg text-white">
                <h2 class="text-3xl font-semibold mb-4">📦 Order Page</h2>
                <p class="mb-4 text-lg">Check and manage your orders in a smooth and easy way.</p>
                <button class="bg-white text-blue-600 py-2 px-6 rounded-full hover:bg-blue-100 transition duration-300" @click="orderPage">
                    Orders 📝
                </button>
            </div>

            <!-- Volume Page Section -->
            <div class="bg-gradient-to-r from-green-500 to-teal-600 p-6 rounded-lg shadow-lg text-white">
                <h2 class="text-3xl font-semibold mb-4">📊 Volume Page</h2>
                <p class="mb-4 text-lg">View your volumes and track progress.</p>
                <button class="bg-white text-green-600 py-2 px-6 rounded-full hover:bg-green-100 transition duration-300" @click="volumePage">
                    Volumes 📈
                </button>
            </div>
        </div>
    </div>
</template>
<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

// Navigate to the delivery page
const orderPage = () => {
    router.push('/customer/orders');
};
const volumePage = () => {
    router.push('/customer/volumes');
};
</script>