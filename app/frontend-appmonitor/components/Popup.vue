<template>
    <!-- Success Popup -->
    <div v-if="type=='success' && show" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 max-w-sm">
            <h2 class="text-lg font-semibold text-green-600">Success!</h2>
            <!-- Render the message with HTML using v-html -->
            <p class="mt-2" v-html="message"></p>
            <button @click="closePopup" class="mt-4 bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">
                Close
            </button>
        </div>
    </div>
    <!-- Failure Popup -->
    <div v-if="type=='failure' && show" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 max-w-sm">
            <h2 class="text-lg font-semibold text-red-600">Error!</h2>
            <!-- Render the message with HTML using v-html -->
            <p class="mt-2" v-html="message"></p>
            <button @click="closePopup" class="mt-4 bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600">
                Close
            </button>
        </div>
    </div>
    <!-- Information Popup -->
    <div v-if="type=='info' && show" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 max-w-sm">
            <h2 class="text-lg font-semibold text-blue-600">{{ title }}</h2>
            <!-- Render the message with HTML using v-html -->
            <p class="mt-2" v-html="message"></p>
            <button @click="closePopup" class="mt-4 bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">
                Close
            </button>
        </div>
    </div>
</template>

<script setup>
import { ref, defineProps } from 'vue';

// Props for dynamic popup message and type
defineProps({
    show: {
        type: Boolean,
        required: true,
    },
    title: {
        type: String,
        required: true,
    },
    message: {
        type: String,
        required: true,
    },
    type: {
        type: String,
        required: true,
    },
});

// Emit event to close the popup
const emit = defineEmits(['close']);

// Method to close the popup
const closePopup = () => {
    emit('close');
};
</script>
