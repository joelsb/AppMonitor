<template>
        <!-- NavBar -->
        <NavBar />

        <!-- Show PackageTypeForm for Creating or Editing -->
      <PackageTypeForm :isCreating="false" :isEditing="false"/>
      <Popup v-if="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import NavBar from '@/components/NavBar.vue';
import PackageTypeForm from '@/components/PackageTypeForm.vue';
import { useRuntimeConfig } from '#imports';

const router = useRouter();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');


// Reactive Data
const packageTypes = ref([]);

// Fetch Package Types
const fetchPackageTypes = async () => {
    try {
        const response = await fetch(`${apiUrl}/package-types`);
        if (!response.ok) {
            let messages = await response.text()    
            popupTitle.value = 'Error';
            popupMessages.value.push(messages);
            popupType.value = 'failure';
            showPopup.value = true;
        }
        packageTypes.value = await response.json();
    } catch (err) {
        popupTitle.value = 'Error';
        popupMessages.value.push(err.message);
        popupType.value = 'failure';
        showPopup.value = true;
    }
};

// Fetch Data on Mount
onMounted(() => {
    fetchPackageTypes();
});

//Close Popup
const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
};

// Go back
const goBack = () => {
    window.history.back();
};
</script>

<style scoped>
.table-container {
    overflow-x: auto;
}

.table {
    border-collapse: collapse;
    width: 100%;
}

.table th,
.table td {
    border: 1px solid #ccc;
    padding: 0.75rem;
}

.table th {
    background-color: #f9f9f9;
}

.table td {
    text-align: left;
}
</style>
