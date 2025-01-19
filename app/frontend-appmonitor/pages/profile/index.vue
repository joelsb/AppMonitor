<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Main Content -->
        <div class="max-w-4xl mx-auto mt-8 p-8 bg-white rounded-lg shadow-xl space-y-8 flex flex-col">
            
            <!-- View Profile Section -->
            <div class="bg-gradient-to-r from-indigo-500 to-purple-600 p-8 rounded-lg shadow-lg text-white">
                <h2 class="text-3xl font-semibold mb-4">👤 See Profile</h2>
                <p class="mb-6 text-lg">View your profile details and personal information.</p>
                <button class="bg-white text-indigo-600 py-3 px-8 rounded-full hover:bg-indigo-100 transition duration-300" @click="router.push('/profile/view-edit')">
                    View Profile 📜
                </button>
            </div>

            <!-- Change Password Section -->
            <div class="bg-gradient-to-r from-green-500 to-teal-600 p-8 rounded-lg shadow-lg text-white">
                <h2 class="text-3xl font-semibold mb-4">🔑 Change Password</h2>
                <p class="mb-6 text-lg">Update your login credentials to enhance security.</p>
                <button class="bg-white text-green-600 py-3 px-8 rounded-full hover:bg-green-100 transition duration-300" @click="router.push('/profile/new-password')">
                    Change Password 🔒
                </button>
            </div>

        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();


</script>



<style scoped></style>
