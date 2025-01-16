<template>
    <!-- Success Popup -->
    <div v-if="type == 'success' && show" class="popup-overlay">
        <div class="popup-box popup-box-success">
            <h2 class="popup-title popup-title-success">Success!</h2>
            <!-- Render the message with HTML using v-html -->
            <ul v-if="messages.length > 0" class="popup-messages">
                <li v-for="(message, index) in messages" :key="index" v-html="message"></li>
            </ul>
            <button @click="closePopup" class="popup-close-button popup-close-button-success">
                Close
            </button>
        </div>
    </div>

    <!-- Failure Popup -->
    <div v-if="type == 'failure' && show" class="popup-overlay">
        <div class="popup-box popup-box-failure">
            <h2 class="popup-title popup-title-failure">Error!</h2>
            <!-- Render the message with HTML using v-html -->
            <ul v-if="messages.length > 0" class="popup-messages">
                <li v-for="(message, index) in messages" :key="index" v-html="message"></li>
            </ul>
            <button @click="closePopup" class="popup-close-button popup-close-button-failure">
                Close
            </button>
        </div>
    </div>

    <!-- Information Popup -->
    <div v-if="type == 'info' && show" class="popup-overlay">
        <div class="popup-box popup-box-info">
            <h2 class="popup-title popup-title-info">{{ title }}</h2>
            <!-- Render the message with HTML using v-html -->
            <ul v-if="messages.length > 0" class="popup-messages">
                <li v-for="(message, index) in messages" :key="index" v-html="message" class="popup-single-message"></li>
            </ul>
            <button @click="closePopup" class="popup-close-button popup-close-button-info">
                Close
            </button>
        </div>
    </div>
</template>


<script setup>
import { ref } from 'vue';


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
    messages: {
        type: Array,
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

<style scoped>
.popup-messages {
    list-style-type: disc;
    margin-left: 25px;
}
.popup-single-message {
    margin-bottom: 7px;
    margin-top: 7px;
}

/* Popup Overlay */
.popup-overlay {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 50;
}

/* Popup Box (General) */
.popup-box {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem; /* space-y-4 */
    padding: 20px;
    background-color: white;
    border-radius: 0.5rem; /* rounded-lg */
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1); /* shadow-lg */
    padding: 1.5rem; /* p-6 */
    max-width: 24rem; /* max-w-sm */
    width: 100%;
}

/* Success Popup */
.popup-box-success {
    background-color: #e7f9e4; /* light green background for success */
}

.popup-title{
    width: 100%;
    text-align: center;
    font-size: 1.5rem; /* text-lg */
    font-weight: 600; /* font-semibold */
    text-transform: uppercase;
}
.popup-title-success {
    color: #16a34a; /* text-green-600 */
}
.popup-close-button {
    width: 100%;
    margin-top: 0;
    color: white;
    padding: 0.5rem 1rem; /* py-2 px-4 */
    border-radius: 0.375rem; /* rounded */
    cursor: pointer;
    transition: background-color 0.2s ease;
}

.popup-close-button-success {
    background-color: #16a34a; /* green background */
}

.popup-close-button-success:hover {
    background-color: #15803d; /* darker green on hover */
}

/* Failure Popup */
.popup-box-failure {
    background-color: #fef2f2; /* light red background for failure */
}

.popup-title-failure {
    color: #dc2626; /* text-red-600 */
}

.popup-close-button-failure {
    background-color: #dc2626; /* red background */
}

.popup-close-button-failure:hover {
    background-color: #b91c1c; /* darker red on hover */
}

/* Information Popup */
.popup-box-info {
    background-color: #eff6ff; /* light blue background for information */
}

.popup-title-info {
    color: #3b82f6; /* text-blue-600 */
}

.popup-close-button-info {
    margin-top: 1rem;
    background-color: #3b82f6; /* blue background */
    color: white;
    padding: 0.5rem 1rem; /* py-2 px-4 */
    border-radius: 0.375rem; /* rounded */
    cursor: pointer;
    transition: background-color 0.2s ease;
}

.popup-close-button-info:hover {
    background-color: #2563eb; /* darker blue on hover */
}

</style>
