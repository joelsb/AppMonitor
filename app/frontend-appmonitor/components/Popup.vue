<template>
    <!-- Success Popup -->
    <div v-if="type == 'success' && show" class="popup-overlay">
        <div class="popup-box popup-box-success">
            <h2 class="popup-title popup-title-success">🎉 Success!</h2>
            <ul v-if="messages.length > 0" class="popup-messages">
                <li v-for="(message, index) in messages" :key="index" v-html="message"></li>
            </ul>
            <button @click="closePopup" class="popup-close-button popup-close-button-success">
                Close 🛑
            </button>
        </div>
    </div>

    <!-- Failure Popup -->
    <div v-if="type == 'failure' && show" class="popup-overlay">
        <div class="popup-box popup-box-failure">
            <h2 class="popup-title popup-title-failure">⚠️ Error!</h2>
            <ul v-if="messages.length > 0" class="popup-messages">
                <li v-for="(message, index) in messages" :key="index" v-html="message"></li>
            </ul>
            <button @click="closePopup" class="popup-close-button popup-close-button-failure">
                Close 🛑
            </button>
        </div>
    </div>

    <!-- Information Popup -->
    <div v-if="type == 'info' && show" class="popup-overlay">
        <div class="popup-box popup-box-info">
            <h2 class="popup-title popup-title-info">{{ title }} 🗣️</h2>
            <ul v-if="messages.length > 0" class="popup-messages">
                <li v-for="(message, index) in messages" :key="index" v-html="message" class="popup-single-message"></li>
            </ul>
            <button @click="closePopup" class="popup-close-button popup-close-button-info">
                Close 🛑
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
    margin-left: 20px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    color: #333;
}

.popup-single-message {
    margin-bottom: 7px;
    margin-top: 7px;
    font-size: 1rem;
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
    background-color: rgba(0, 0, 0, 0.7);
    z-index: 50;
    animation: fadeIn 0.5s ease;
}

/* Popup Box (General) */
.popup-box {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    padding: 20px;
    background-color: white;
    border-radius: 1rem;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
    max-width: 28rem;
    width: 100%;
    animation: popupScale 0.3s ease-in-out;
}

/* Success Popup */
.popup-box-success {
    background-color: #e0f7e6; /* light green background */
}

.popup-title-success {
    color: #16a34a; /* green text */
}

/* Failure Popup */
.popup-box-failure {
    background-color: #fef2f2; /* light red background */
}

.popup-title-failure {
    color: #dc2626; /* red text */
}

/* Information Popup */
.popup-box-info {
    background-color: #eff6ff; /* light blue background */
}

.popup-title-info {
    color: #3b82f6; /* blue text */
}

/* Close Button (General) */
.popup-close-button {
    margin-top: 1rem;
    padding: 0.75rem 1.5rem;
    font-size: 1.1rem;
    border-radius: 0.375rem;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
    width: 100%;
    text-align: center;
}

.popup-close-button:hover {
    opacity: 0.8;
}

/* Success Button */
.popup-close-button-success {
    background-color: #16a34a;
    color: white;
}

.popup-close-button-success:hover {
    background-color: #15803d;
}

/* Failure Button */
.popup-close-button-failure {
    background-color: #dc2626;
    color: white;
}

.popup-close-button-failure:hover {
    background-color: #b91c1c;
}

/* Info Button */
.popup-close-button-info {
    background-color: #3b82f6;
    color: white;
}

.popup-close-button-info:hover {
    background-color: #2563eb;
}

/* Animations */
@keyframes fadeIn {
    0% {
        opacity: 0;
    }
    100% {
        opacity: 1;
    }
}

@keyframes popupScale {
    0% {
        transform: scale(0.9);
    }
    100% {
        transform: scale(1);
    }
}

</style>