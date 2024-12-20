<template>
    <div class="flex">
        <nav class="flex justify-between items-center border-b border-gray-300 bg-gray-100 p-4 w-full">
            <!-- Login Button (Left-aligned) -->
            <div class="flex-shrink-0">
                <button class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-all"
                    @click="login">
                    Login
                </button>
            </div>

            <!-- Navigation Links (Centered) -->
            <ul class="flex space-x-6 mx-auto">
                <li>
                    <NuxtLink to="/customer"
                        class="font-semibold text-blue-500 no-underline hover:bg-blue-100 hover:rounded-full hover:px-4 hover:py-2 transition-all">
                        Customer
                    </NuxtLink>
                </li>
                <li>
                    <NuxtLink to="/manager"
                        class="font-semibold text-blue-500 no-underline hover:bg-blue-100 hover:rounded-full hover:px-4 hover:py-2 transition-all">
                        Manager
                    </NuxtLink>
                </li>
                <li>
                    <NuxtLink to="/employee"
                        class="font-semibold text-blue-500 no-underline hover:bg-blue-100 hover:rounded-full hover:px-4 hover:py-2 transition-all">
                        Employee
                    </NuxtLink>
                </li>
            </ul>
        </nav>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
function login() {
    router.push('/login');
}
</script>