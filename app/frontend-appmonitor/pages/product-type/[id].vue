<template>
    <!-- NavBar -->
    <NavBar />
  
    <!-- Show ProductTypeForm for Creating or Editing -->
    <ProductTypeForm :create="false" :edit="false" />
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import NavBar from '@/components/NavBar.vue';
  import ProductTypeForm from '@/components/ProductTypeForm.vue';
  import { useRuntimeConfig } from '#imports';
  
  const router = useRouter();
  const config = useRuntimeConfig();
  const apiUrl = config.public.API_URL;
  
  </script>
  
  <style scoped>
  /* Scoped styles for this page */
  </style>
  