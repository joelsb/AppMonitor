<template>
  <div>
    <nav class="navbar" :class="{ 'nav-bar-when-sm': hide }">
      <!-- Hamburger Button for Mobile (Visible on screens smaller than 540px) -->
      <div class="hamburger-menu" @click="toggleMenu">
        <div class="hamburger-icon" :class="{ 'open': !menuOpen }">
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </div>
      </div>
      
      <!-- Navbar Links (Only show on larger screens) -->
      <ul class="nav-links" :class="{ 'hidden': menuOpen }">
        <li class="nav-item" :class="{ 'hidden': !hide }">
          <button class="nav-button nav-button-hidden">
          </button>
        </li>
        <div class="profile-item left">
          <li class="nav-item">
            <button class="nav-button profile-button" @click="navigate('/homepage')">
              <svg xmlns="http://www.w3.org/2000/svg" class="user-avatar" height="24px" viewBox="0 -960 960 960"
                width="24px" fill="#3b82f6">
                <path
                  d="M240-200h120v-240h240v240h120v-360L480-740 240-560v360Zm-80 80v-480l320-240 320 240v480H520v-240h-80v240H160Zm320-350Z" />
              </svg>
            </button>
          </li>
        </div>
        
        <!-- Regular links (excluding Profile) -->
        <li v-for="(link, index) in filteredLinks" :key="index" class="nav-item">
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
        <div class="profile-item right" :class="{ 'profile-when-sm': hide }">
          <!-- Profile link (the 4th link in the array) -->
          <li v-if="linkProfile" class="nav-item">
            <button :class="[ 
              'nav-button profile-button',
              (activeIndex === 3 || linkProfile.active) ? 'active' : ''
            ]" @click="navigate(linkProfile.route)">
              <svg xmlns="http://www.w3.org/2000/svg" class="user-avatar" height="24px" viewBox="0 -960 960 960"
                width="24px" fill="#3b82f6">
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
const menuOpen = ref(false); // Reactive state for menu visibility on small screens
const hide = ref(false);

const user = useAuthStore().user;// Obter o utilizador logado
const filteredLinks = computed(() => {
  if (!user || !user.role) return []; // Nenhum usuário ou sem role
  return links.filter(link => link.roles.includes(user.role));
});

const links = [
  {
    name: 'Customer',
    route: '/customer',
    active: false,
    roles: ['Customer','Admin'], // Apenas usuários com role 'customer' podem acessar
    submenu: [
      { name: 'Show Orders', route: '/customer/orders' },
      { name: 'Show Volumes', route: '/customer/volumes' },
    ],
  },
  {
    name: 'Manager',
    route: '/manager',
    active: false,
    roles: ['Manager','Admin'], // Apenas usuários com role 'manager' podem acessar
    submenu: [
      { name: 'Show Orders', route: '/manager/orders' },
      { name: 'Show Volumes', route: '/manager/volumes' },
    ],
  },
  {
    name: 'Employee',
    route: '/employee',
    active: false,
    roles: ['Employee','Admin'], // Apenas usuários com role 'employee' podem acessar
    submenu: [
      { name: 'New Volume', route: '/employee/newVolume' },
      { name: 'Deliver', route: '/employee/deliver' },
    ],
  },
  {
    name: 'Admin',
    route: '/admin',
    active: false,
    roles: ['Admin'], // Apenas usuários com role 'admin' podem acessar
    submenu: [
      { name: 'Users', route: '/user' },
      { name: 'Package Types', route: '/package-type' },
      { name: 'Product Types', route: '/product-type' },
      { name: 'Sensors', route: '/sensor' },
    ]
  } 
];


const linkProfile =
{
  name: 'Profile',
  route: '/profile',
  active: false,
  submenu: [
    { name: 'Profile', route: '/profile/view-edit' },
    { name: 'Change Password', route: '/profile/new-password' },
    { name: 'Logout', route: '/login/logout' },
  ],
}

// Toggle menu visibility for small screens
function toggleMenu() {
  menuOpen.value = !menuOpen.value;
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
//watch hide value
watch(hide, (newHide) => {
  console.log(newHide);
  });
// Login button handler
function onLogin() {
  router.push('/login');
}

function onLogout() {
  useAuthStore().logout();
}

// Window resize handler to close menu when screen width is small
function handleResize() {
  if (window.innerWidth > 540) {
    menuOpen.value = false;
    hide.value = false;
  }
  if(window.innerWidth < 540){
    menuOpen.value = true;
    hide.value = true;
  }
}
// Ensure window resize handler is attached when the component is mounted
onMounted(() => {
  if (window.innerWidth > 540) {
    menuOpen.value = false;
    hide.value = false;
  }
  if(window.innerWidth < 540){
    menuOpen.value = true;
    hide.value = true;
  }
  window.addEventListener('resize', handleResize);
});

// Cleanup the resize listener when the component is destroyed
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
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
  fill: #3b82f6;
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
}

.right {
  justify-content: flex-end;
}

.profile-item {
  flex-basis: 0;
  flex-grow: 1;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.left {
  justify-content: flex-start;
}

.profile-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
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

.nav-button-hidden {
  background-color: transparent !important;
}

.nav-button:hover {
  background-color: #bfdbfe;
  /* blue-100 */
  border-radius: 9999px;
  padding-left: 0.5rem;
  padding-right: 0.5rem;
}

.nav-button.active {
  color: #2563eb;
  /* blue-600 */
  background-color: #bfdbfe;
  /* blue-100 */
  border-radius: 9999px;
  padding-left: 0.5rem;
  padding-right: 0.5rem;
}

/* Submenu */
.submenu {
  position: absolute;
  left: 50%;
  /* Start positioning from the horizontal center of the parent */
  transform: translateX(-50%);
  /* Shift the submenu to center */
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
  transition: all 0.2s ease;
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
  background-color: #bfdbfe;
  /* blue-100 */
  border-radius: 9999px;
  padding-left: 0.7rem;
  padding-right: 0.7rem;
  scale: 1.05;
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

/* Hamburger Icon */
.hamburger-menu {
  z-index: 200;
  padding: 0.5rem 0;
  margin-bottom: 1rem;
  display: none;
  cursor: pointer;
}

.hamburger-icon {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.bar {
  width: 20px;
  height: 3px;
  background-color: #000000;
  transition: 0.3s;
}

.hamburger-icon.open .bar:nth-child(1) {
  transform: rotate(45deg) translateY(9.5px);
}

.hamburger-icon.open .bar:nth-child(2) {
  opacity: 0;
}

.hamburger-icon.open .bar:nth-child(3) {
  transform: rotate(-45deg) translateY(-10px);
}

/* Navbar links visibility on small screens */
.nav-links.hidden {
  display: none;
}
.hidden {
  display: none !important;
}

.nav-bar-when-sm {
  background-color: white;
  border: #000000;
}

.profile-when-sm {
  justify-content: flex-start !important;
}


@media (max-width: 540px) {
  /* Show hamburger icon on smaller screens */
  .hamburger-menu {
    display: block;
  }

  /* Hide nav-links by default */
  .nav-links {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    background-color: white;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    padding: 10px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    z-index: 100;
    transform: translateX(-100%);
    transition: all 0.3s ease;
  }

  /* Show nav-links when menuOpen is true */
  .nav-links:not(.hidden) {
    transform: translateX(0);
  }
}
</style>