<template>
  <div class="card">
    <div class="mb-4 flex-row justify-between flex items-center">
      <button @click="router.go(-1)" class="px-6 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
      🔙 Back
      </button>
      <div v-if="!isCreating" class="flex justify-end items-center">
        <label for="editProfileSwitch" class="block font-semibold text-lg mr-3">Edit Mode</label>
        <label for="editProfileSwitch" class="inline-flex relative items-center cursor-pointer">
          <input type="checkbox" id="editProfileSwitch" v-model="isEditing" class="sr-only" />
          <div class="w-14 h-6 rounded-full transition-colors" :class="isEditing ? 'bg-blue-500' : 'bg-gray-200'"></div>
          <div class="absolute left-1 top-1 w-4 h-4 bg-white rounded-full transition-transform"
            :class="isEditing ? 'transform translate-x-7' : ''"></div>
        </label>
        <span class="ml-4 text-lg">{{ isEditing ? 'Editing' : 'View' }}</span>
      </div>
    </div>

    <!-- Package Type Form -->
    <form @submit.prevent="handleSubmit" class="form">
      <!-- Package Type ID -->
      <div class="mb-4">
        <label for="id" class="block text-gray-700">Package Type ID</label>
        <input type="number" min="1" id="id" v-model="packageType.id" class="form-control"
          :disabled="!isEditing && !isCreating" />
      </div>
      <div class="mb-4">
        <label for="name" class="block text-gray-700">Package Type Name</label>
        <input type="text" id="name" v-model="packageType.name" class="form-control" required
          :disabled="!isEditing && !isCreating" />
      </div>

      <!-- Submit Button -->
      <!-- Save Button (Only in Edit Mode) -->
      <div v-if="isEditing && isCreating" class="edit-button">
        <button type="submit" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">
          Create new Package Type
        </button>
      </div>
      <!-- Create Button (Only in Create Mode) -->
      <div v-if="isEditing && !isCreating" class="edit-button">
        <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">
          💾 Save Changes
        </button>
      </div>
    </form>
  </div>
  <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useRuntimeConfig } from '#imports';

const router = useRouter();
const route = useRoute();
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const props = defineProps({
  isCreating: Boolean, // Initial data
  isEditing: Boolean,
});

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');

// Reactive data
const packageType = ref({ id: 1, name: '' });
const isEditing = ref(props.isEditing);
const isCreating = ref(props.isCreating);

// Fetch Package Type Data (if editing)
const fetchPackageType = async (id) => {
  try {
    const response = await fetch(`${apiUrl}/package-types/${id}`);
    if (!response.ok) {
      let messages = await response.text()
      popupTitle.value = 'Error';
      popupMessages.value.push(messages);
      popupType.value = 'failure';
      showPopup.value = true;
    }
    packageType.value = await response.json();
  } catch (err) {
    popupTitle.value = 'Error';
    popupMessages.value.push(err.message);
    popupType.value = 'failure';
    showPopup.value = true;
  }
};

// Create or Edit Package Type
const handleSubmit = async () => {
  try {
    const method = isEditing.value && isCreating.value ? 'POST' : 'PUT';
    const url = isEditing.value && isCreating.value
      ? `${apiUrl}/package-types`
      : `${apiUrl}/package-types/${packageType.value.id}`;

    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(packageType.value),
    });

    if (!response.ok) {
      let messages = await response.text()
      popupTitle.value = 'Error';
      popupMessages.value.push(messages);
      popupType.value = 'failure';
      showPopup.value = true;
    }
    else {
      popupTitle.value = 'Success';
      popupMessages.value.push('Package Type saved successfully');
      popupType.value = 'success';
      showPopup.value = true;

    }

  } catch (err) {
    popupTitle.value = 'Error';
    popupMessages.value.push(err.message);
    popupType.value = 'failure';
    showPopup.value = true;
  }
};

// On component mount, check if editing or creating
onMounted(() => {
  if (route.params.id) {
    // Editing mode
    fetchPackageType(route.params.id);
  } else {
    // Creating a new package type
    isCreating.value = true;
    packageType.value = { id: null, name: '' };
  }
});

// Close Popup
const closePopup = () => {
  showPopup.value = false;
  popupMessages.value = [];
  router.go(-1);
};

</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.edit-button {
  display: flex;
  justify-content: flex-end;
}

.card {
  max-width: 600px;
  margin: 2rem auto;
  padding: 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.card-header h2 {
  text-align: center;
}

.form-control {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 0.375rem;
  font-size: 1rem;
}

.form-messages ul {
  color: red;
  list-style: none;
  padding: 0;
  margin-top: 1rem;
}
</style>
