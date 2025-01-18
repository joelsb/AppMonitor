<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Card to choose delivery type (Volume or Order) -->
        

        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md gap-6 flex flex-col ">
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-2xl font-semibold mb-4">User Page</h2>
                <p class="mb-4 text-lg text-gray-600">See all the users.</p>
                <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="userPage">
                    Users</button>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-2xl font-semibold mb-4">New User Page</h2>
                <p class="mb-4 text-lg text-gray-600">See all the users.</p>
                <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="newUserPage">
                    New User</button>
                </div>
        </div>
    </div>
</template>
<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

// Navigate to the delivery page
const userPage = () => {
    router.push('/admin/users');
};
const newUserPage = () => {
    router.push('/admin/newUser');
};
</script>