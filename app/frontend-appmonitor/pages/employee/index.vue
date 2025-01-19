<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Main Content -->
        <div class="max-w-4xl mx-auto mt-8 p-8 bg-white rounded-lg shadow-xl space-y-8 flex flex-col">
            
            <!-- Delivery Page Section -->
            <div class="bg-gradient-to-r from-indigo-500 to-purple-600 p-8 rounded-lg shadow-lg text-white">
                <h2 class="text-3xl font-semibold mb-4">🚚 Delivery Page</h2>
                <p class="mb-6 text-lg">Manage and track the delivery process for your volumes with ease.</p>
                <button class="bg-white text-indigo-600 py-3 px-8 rounded-full hover:bg-indigo-100 transition duration-300" @click="deliverPage">
                    Deliver Now 📦
                </button>
            </div>

            <!-- New Order/Volume Page Section -->
            <div class="bg-gradient-to-r from-green-500 to-teal-600 p-8 rounded-lg shadow-lg text-white">
                <h2 class="text-3xl font-semibold mb-4">➕ New Order/Volume Page</h2>
                <p class="mb-6 text-lg">Add new orders or volumes and track them in the system seamlessly.</p>
                <button class="bg-white text-green-600 py-3 px-8 rounded-full hover:bg-green-100 transition duration-300" @click="newVolumePage">
                    Add Volume/Order 📊
                </button>
            </div>

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