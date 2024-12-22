<template>
  <div class="flex">
    <nav class="flex justify-between items-center border-b border-gray-300 bg-gray-100 p-4 w-full">
      <!-- Login Button (Left-aligned) -->
      <div class="flex-shrink-0">
        <button class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-all" @click="onLogin">
          Login
        </button>
      </div>

      <!-- Navigation Links (Centered) -->
      <ul class="flex space-x-6 mx-auto relative">
        <li
          v-for="(link, index) in links"
          :key="index"
          class="relative group">
          <button :class="[ 
            'py-2 font-semibold no-underline hover:bg-blue-100 hover:rounded-full hover:px-4 transition-all', 
            (activeIndex === index || link.active) ? 'text-blue-600 bg-blue-100 rounded-full px-4' : 'text-blue-500'
          ]" @click="navigate(link.route)">
            {{ link.name }}
          </button>

          <!-- Submenu -->
          <ul
            v-if="link.submenu"
            class="absolute left-0 top-full bg-white shadow-lg rounded-md p-2 space-y-2 w-40 opacity-0 group-hover:opacity-100 group-hover:block transition-all duration-200 z-50 hidden">
            <li
              v-for="(item, subIndex) in link.submenu"
              :key="subIndex"
              class="hover:bg-gray-100 px-4 py-2 rounded-lg cursor-pointer"
              @click="navigate(item.route)"
            >
              {{ item.name }}
            </li>
          </ul>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const router = useRouter();
const activeIndex = ref(null);
const route = useRoute();

// Navigation links and submenus
const links = [
  {
    name: 'Customer',
    route: '/customer',
    active: false,
    submenu: [
      { name: 'Show Orders', route: '/customer/orders' },
      { name: 'Show Volumes', route: '/customer/volumes' },
      { name: 'Show Sensors', route: '/customer/sensors' },
    ],
  },
  {
    name: 'Manager',
    route: '/manager',
    active: false,
    submenu: [
      { name: 'Show Orders', route: '/manager/orders' },
      { name: 'Show Volumes', route: '/manager/volumes' },
      { name: 'Show Sensors', route: '/manager/sensors' },
    ],
  },
  {
    name: 'Employee',
    route: '/employee',
    active: false,
    submenu: [
      { name: 'New Volume', route: '/employee/newVolume' },
      { name: 'Deliver', route: '/employee/deliver' },
    ],
  },
];

// Update active index for both parent and submenu items
function setActiveIndex(index) {
  activeIndex.value = index;
}

// Navigate handler
function navigate(route) {
  router.push(route);
}

// Watch route changes to dynamically mark active navigation
watch(route, (newRoute) => {
  links.forEach(link => {
    link.active = newRoute.path.includes(link.route);
  });
});

// Login button handler
function onLogin() {
  router.push('/login');
}
</script>

