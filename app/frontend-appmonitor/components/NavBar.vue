<template>
  <div>
    <nav class="navbar">
      <!-- Login Button (Left-aligned) -->
      <ul class="nav-links">
        <div class="profile-item">
        </div>
        <!-- Regular links (excluding Profile) -->
        <li v-for="(link, index) in links" :key="index" class="nav-item">
          <button v-if="link.name !== 'Profile'" :class="[
            'nav-button',
            (activeIndex === index || link.active) ? 'active' : ''
          ]" @click="navigate(link.route)">
            {{ link.name }}
          </button>

          <!-- Submenu for other links -->
          <ul v-if="link.submenu || link.name !== 'Profile'" class="submenu">
            <li v-for="(item, subIndex) in link.submenu" :key="subIndex" class="submenu-item"
              @click="navigate(item.route)">
              {{ item.name }}
            </li>
          </ul>
        </li>

        <!-- Profile link -->
        <div class="profile-item">
          <!-- Profile link (the 4th link in the array) -->
          <li v-if="linkProfile" class="nav-item">
            <button :class="[
              'nav-button profile-button',
              (activeIndex === 3 || linkProfile.active) ? 'active' : ''
            ]" @click="navigate(linkProfile.route)">
              <svg xmlns="http://www.w3.org/2000/svg" class="user-avatar" height="24px" viewBox="0 -960 960 960"
                width="24px" fill="#5f6368">
                <path
                  d="M480-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM160-160v-112q0-34 17.5-62.5T224-378q62-31 126-46.5T480-440q66 0 130 15.5T736-378q29 15 46.5 43.5T800-272v112H160Zm80-80h480v-32q0-11-5.5-20T700-306q-54-27-109-40.5T480-360q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T560-640q0-33-23.5-56.5T480-720q-33 0-56.5 23.5T400-640q0 33 23.5 56.5T480-560Zm0-80Zm0 400Z" />
              </svg>
              {{ user.name }}
            </button>
            <ul v-if="linkProfile.submenu" class="submenu profile-submenu">
              <li v-for="(item, subIndex) in linkProfile.submenu" :key="subIndex" class="submenu-item"
                @click="navigate(item.route)">
                {{ item.name }}
              </li>
            </ul>
          </li>
        </div>
      </ul>
    </nav>
  </div>
</template>



<script setup>
import { useRouter } from 'vue-router';
import { ref, watch } from 'vue';
import { useAuthStore } from "~/store/auth-store.js"


const router = useRouter();
const activeIndex = ref(null);
const route = useRoute();

const user = useAuthStore().user

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
  }
];

const linkProfile =
{
  name: 'Profile',
  route: '/profile',
  active: false,
  submenu: [
    { name: 'Profile', route: '/profile' },
    { name: 'Change Password', route: '/profile/password' },
    { name: 'Logout', route: '/login/logout' },
  ],
}

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

function onLogout() {
  useAuthStore().logout();
}
</script>


<style scoped>
/* User Menu container */
.user-menu {
  display: flex;
  align-items: center;
}

/* User Avatar */
.user-avatar {
  width: 24px;
  height: 24px;
  fill: #5f6368;
  margin-right: 8px;
}

/* Username styling */
.username {
  font-weight: bold;
  cursor: pointer;
}

/* Dropdown menu container */
.dropdown {
  position: relative;
}

/* Initially hide the dropdown menu */
.dropdown-menu {
  display: none;
  list-style-type: none;
  padding: 0;
  margin: 0;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 150px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

/* Dropdown item styling */
.dropdown-item {
  padding: 10px;
  cursor: pointer;
  color: #5f6368;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}

/* Show the dropdown menu when hovering over the parent (username) */
.dropdown:hover .dropdown-menu {
  display: block;
}



/* Navbar */
.navbar {
  display: flex;
  flex-direction: row;
  align-items: center;
  border-bottom: 1px solid #e5e7eb;
  /* gray-300 */
  background-color: #f3f4f6;
  /* gray-100 */
  padding: 1rem 1rem 0rem 1rem;
  width: 100%;
}

/* Login Button */
.login-btn {
  flex-shrink: 0;
}

.login-button {
  background-color: #3b82f6;
  /* blue-500 */
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  transition: all 0.3s ease;
}

.login-button:hover {
  background-color: #2563eb;
  /* blue-600 */
}

/* Navigation Links */
.nav-links {
  flex-grow: 1;
  display: flex;
  flex-direction: row;
  gap: 1.5rem;
  /* space-x-6 */
  position: relative;
}

.nav-item {
  position: relative;
}

.profile-item {
  flex-basis: 0;
  flex-grow: 1;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-end;
}

.profile-button {
  display: flex;
  align-items: center;
}

.nav-button {
  padding: 0.5rem 0.1rem;
  margin-bottom: 1rem;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  color: #3b82f6;
  /* blue-500 */
}

.nav-button:hover {
  background-color: #bfdbfe;
  /* blue-100 */
  border-radius: 9999px;
  padding-left: 1rem;
  padding-right: 1rem;
}

.nav-button.active {
  color: #2563eb;
  /* blue-600 */
  background-color: #bfdbfe;
  /* blue-100 */
  border-radius: 9999px;
  padding-left: 1rem;
  padding-right: 1rem;
}

/* Submenu */
.submenu {
  position: absolute;
  left: 50%; /* Start positioning from the horizontal center of the parent */
  transform: translateX(-50%); /* Shift the submenu to center */
  top: 90%;
  background-color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 0.375rem;
  /* rounded-md */
  padding: 0.5rem;
  gap: 0.5rem;
  /* space-y-2 */
  width: 11rem;
  /* w-40 */
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  z-index: 50;
}

.profile-submenu {
  right: 0 !important;
  left: auto !important;
  transform: translateX(0) !important;
}

.nav-item:hover .submenu {
  opacity: 1;
  visibility: visible;
}

.nav-item:hover .nav-button {
  background-color: #bfdbfe; /* blue-100 */
  border-radius: 9999px;
  padding-left: 1rem;
  padding-right: 1rem;
}

.submenu-item {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  /* rounded-lg */
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submenu-item:hover {
  background-color: #f3f4f6;
  /* gray-100 */
}
</style>