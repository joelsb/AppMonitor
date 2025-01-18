<template>
    <!-- NavBar (if necessary) -->
    <NavBar />

    <!-- Card to choose delivery type (Volume or Order) -->

    <div class="max-w-4xl mx-auto mt-6 p-8 bg-white rounded-lg shadow-lg space-y-6 flex flex-col">
        
        <!-- User Page Section -->
        <div class="bg-gradient-to-r from-blue-500 to-indigo-600 p-6 rounded-lg shadow-lg text-white">
            <h2 class="text-3xl font-semibold mb-4">👥 User Page</h2>
            <p class="mb-4 text-lg"><span class="font-bold">See</span>, <span class="font-bold">edit</span>, and <span class="font-bold">create</span> Users in the App Monitor</p>
            <button class="bg-white text-blue-600 py-2 px-6 rounded-full hover:bg-blue-100 transition duration-300" @click="router.push('/user')">
                Manage Users 📋
            </button>
        </div>

        <!-- Package Types Section -->
        <div class="bg-gradient-to-r from-green-500 to-teal-600 p-6 rounded-lg shadow-lg text-white">
            <h2 class="text-3xl font-semibold mb-4">📦 Manage Package Types</h2>
            <p class="mb-4 text-lg"><span class="font-bold">See</span>, <span class="font-bold">edit</span>, and <span class="font-bold">create</span> Package Types in App Monitor</p>
            <button class="bg-white text-green-600 py-2 px-6 rounded-full hover:bg-green-100 transition duration-300" @click="router.push('/package-type')">
                Package Types 🏷️
            </button>
        </div>

        <!-- Product Types Section -->
        <div class="bg-gradient-to-r from-yellow-500 to-orange-600 p-6 rounded-lg shadow-lg text-white">
            <h2 class="text-3xl font-semibold mb-4">🍎 Product Type Page</h2>
            <p class="mb-4 text-lg"><span class="font-bold">See</span>, <span class="font-bold">edit</span>, and <span class="font-bold">create</span> Product Types in App Monitor</p>
            <button class="bg-white text-yellow-600 py-2 px-6 rounded-full hover:bg-yellow-100 transition duration-300" @click="router.push('/product-type')">
                Product Types 🏷️
            </button>
        </div>

        <!-- Sensor Page Section -->
        <div class="bg-gradient-to-r from-purple-500 to-pink-600 p-6 rounded-lg shadow-lg text-white">
            <h2 class="text-3xl font-semibold mb-4">🔧 Sensor Page</h2>
            <p class="mb-4 text-lg"><span class="font-bold">See</span>, and <span class="font-bold">add value</span> to a Sensor in App Monitor</p>
            <button class="bg-white text-purple-600 py-2 px-6 rounded-full hover:bg-purple-100 transition duration-300" @click="router.push('/sensor')">
                Sensors 🔌
            </button>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

const productTypePage = () => {
    router.push('/admin/productType');
};
const newProductTypePage = () => {
    router.push('/admin/newProductType');
};
</script>