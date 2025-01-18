<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Card to choose delivery type (Volume or Order) -->
        

        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md gap-6 flex flex-col">
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-2xl font-semibold mb-4">See Profile</h2>
                <p class="mb-4 text-lg text-gray-600">See my profile details</p>
                <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="profilePage">
                    Profile</button>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-2xl font-semibold mb-4">Change Password</h2>
                <p class="mb-4 text-lg text-gray-600">Change my password login</p>
                <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="passwordPage">
                    Change-Password</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

// Navigate to the delivery page
const profilePage = () => {
    router.push('/profile/view-edit');
};
const passwordPage = () => {
    router.push('/profile/new-password');
};

</script>
<style scoped></style>
