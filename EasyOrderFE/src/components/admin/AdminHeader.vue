<template>
  <header class="mb-3">
    <nav class="navbar navbar-expand navbar-light">
      <div class="container-fluid">
        <a class="burger-btn d-block" @click="$emit('toggle-sidebar')">
          <i class="bi bi-justify fs-3"></i>
        </a>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
            <li class="nav-item dropdown me-1">
              <a
                class="nav-link active dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                id="mailDropdown"
              >
                <i class="bi bi-envelope bi-sub fs-4 text-gray-600"></i>
              </a>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="mailDropdown">
                <li>
                  <h6 class="dropdown-header">Mail</h6>
                </li>
                <li><a class="dropdown-item">No new mail</a></li>
              </ul>
            </li>
            <li class="nav-item dropdown me-3">
              <a
                class="nav-link active dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                id="notificationDropdown"
              >
                <i class="bi bi-bell bi-sub fs-4 text-gray-600"></i>
              </a>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown">
                <li>
                  <h6 class="dropdown-header">Notifications</h6>
                </li>
                <li><a class="dropdown-item">No notification available</a></li>
              </ul>
            </li>
          </ul>
          <div class="dropdown">
            <a
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
              id="userDropdown"
            >
              <div class="user-menu d-flex">
                <div class="user-name text-end me-3">
                  <h6 class="mb-0 text-gray-600">{{ userInfo.name }}</h6>
                  <p class="mb-0 text-sm text-gray-600">{{ userInfo.role }}</p>
                </div>
                <div class="user-img d-flex align-items-center">
                  <div class="avatar avatar-md">
                    <img :src="userInfo.avatar" />
                  </div>
                </div>
              </div>
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
              <li>
                <h6 class="dropdown-header">Hello, {{ userInfo.name }}!</h6>
              </li>

              <li>
                <hr class="dropdown-divider" />
              </li>
              <li>
                <a class="dropdown-item" @click="$emit('logout')">
                  <i class="icon-mid bi bi-box-arrow-left me-2"></i> Logout
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

export default {
  name: 'AdminHeader',
  props: {
    userInfo: {
      type: Object,
      required: true,
      default: () => ({
        name: 'Người dùng',
        role: 'Admin',
        avatar: '/src/assets/images/faces/1.jpg',
      }),
    },
  },
  methods: {
    handleMailClick() {
      console.log('Mail clicked')
    },
  },
}
</script>

<style scoped>
a {
  text-decoration: none;
  cursor: pointer;
}
</style> 