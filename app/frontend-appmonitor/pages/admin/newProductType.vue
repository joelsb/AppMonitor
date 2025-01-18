<template>
    <NavBar />
    <ProductTypeForm 
        :productTypeData="productTypeForm" 
        @createProductType="createProductType"
        :editProductType="editProductType" 
        :create="true"
    />
    
    <Popup 
        :show="showPopup" 
        :title="popupTitle" 
        :messages="popupMessages" 
        :type="popupType" 
        @close="closePopup" 
    />
</template>

<script setup>
import { useRuntimeConfig } from '#imports';
import { ref } from 'vue';
import NavBar from '~/components/NavBar.vue';
import ProductTypeForm from '~/components/ProductTypeForm.vue';
import Popup from '~/components/Popup.vue';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');

const editProductType = ref(true); // Always in "edit mode" for creating a product type
const productTypeForm = ref({ 
    id: '', // Unique identifier
    name: '', // Name of the product type
    mandatorySensors: [], // List of mandatory sensors
});

const createProductType = async (formValue) => {
    popupMessages.value = [];
    try {
        const response = await fetch(`${apiUrl}/product-types`, {  // POST to /product-types
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formValue),
        });

        const data = response.ok ? await response.json() : await response.text();
        if (!response.ok) {
            showPopup.value = true;
            popupTitle.value = 'Error';
            popupMessages.value.push(data || 'Failed to create product type.');
            popupType.value = 'failure';
        } else {
            showPopup.value = true;
            popupTitle.value = 'Success';
            popupMessages.value.push('Product type created successfully!');
            popupType.value = 'success';
        }
    } catch (error) {
        showPopup.value = true;
        popupTitle.value = 'Error';
        popupMessages.value.push('An error occurred while creating the product type.');
        popupType.value = 'error';
    }
};

const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
};
</script>
