<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Main Content -->
        <div class="max-w-4xl mx-auto mt-10 p-8 bg-white rounded-lg shadow-2xl space-y-10 flex flex-col">

            <!-- Order Page Card -->
            <div class="bg-gradient-to-r from-blue-400 to-indigo-600 p-8 rounded-lg shadow-lg hover:shadow-2xl transition-shadow duration-300 ease-in-out text-white">
                <h2 class="text-3xl font-bold mb-6">📝 Order Page</h2>
                <p class="mb-6 text-lg">Efficiently view, manage, and track all your orders in the system.</p>
                <button class="bg-white text-blue-600 py-3 px-8 rounded-full hover:bg-blue-100 transition duration-300" @click="orderPage">
                    View Orders 📦
                </button>
            </div>

            <!-- Volume Page Card -->
            <div class="bg-gradient-to-l from-teal-400 to-green-600 p-8 rounded-lg shadow-lg hover:shadow-2xl transition-shadow duration-300 ease-in-out text-white">
                <h2 class="text-3xl font-bold mb-6">📊 Volume Page</h2>
                <p class="mb-6 text-lg">Manage and review the volumes in your system seamlessly.</p>
                <button class="bg-white text-teal-600 py-3 px-8 rounded-full hover:bg-teal-100 transition duration-300" @click="volumePage">
                    View Volumes 📈
                </button>
            </div>

        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

// Navigate to the order page
const orderPage = () => {
    router.push('/manager/orders');
};

// Navigate to the volume page
const volumePage = () => {
    router.push('/manager/volumes');
};
</script>

<style scoped>
/* Main container for the layout */
.max-w-4xl {
    margin-top: 8rem;
}

/* Button Styles */
.bg-white {
    background-color: white;
}

.text-blue-600, .text-teal-600 {
    color: #3182ce;
}

.bg-blue-600:hover, .bg-teal-600:hover {
    background-color: #2b6cb0;
}

/* Button Styles for Hover Effects */
.bg-blue-600, .bg-teal-600 {
    background-color: #3182ce;
}

.text-blue-600:hover {
    background-color: #2b6cb0;
    transform: translateY(-4px);
}

.text-teal-600:hover {
    background-color: #2c7a7b;
    transform: translateY(-4px);
}

/* Section Card Styling */
.bg-gradient-to-r {
    border-radius: 1rem;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease;
}

/* Hover effect on card */
.bg-gradient-to-r:hover {
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

/* Card content (Text & Buttons) */
h2 {
    font-size: 2rem;
    font-weight: bold;
}

.text-lg {
    font-size: 1.125rem;
    font-weight: 400;
}

/* Flex container styles */
.flex-col {
    display: flex;
    flex-direction: column;
}

/* Margin spacing */
.mb-4 {
    margin-bottom: 1rem;
}

.gap-10 {
    gap: 2.5rem;
}

.max-w-4xl {
    max-width: 48rem;
    margin: 0 auto;
}

/* Hover effect on buttons */
.hover\:shadow-2xl {
    box-shadow: 0 16px 32px rgba(0, 0, 0, 0.2);
}

/* Add spacing and improved hover effects */
.transition-shadow {
    transition: box-shadow 0.3s ease;
}

.duration-300 {
    transition-duration: 300ms;
}

/* Background Color Customization for Each Card */
.bg-gradient-to-r {
    background-image: linear-gradient(to right, #3182ce, #4c51bf);
}

/* Styling the text for buttons */
h2 {
    font-size: 2.25rem;
    font-weight: 600;
    margin-bottom: 1.25rem;
}

.text-gray-600 {
    color: #4a4a4a;
}

/* Extra padding for cards */
.p-8 {
    padding: 2rem;
}

/* Added rounded corners for buttons */
.rounded-full {
    border-radius: 9999px;
}
</style>
