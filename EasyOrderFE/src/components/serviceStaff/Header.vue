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
            <li class="nav-item dropdown me-3">
              <a
                class="nav-link active dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                id="notificationDropdown"
                @click="markNotificationsAsRead"
              >
                <i class="bi bi-bell bi-sub fs-4 text-gray-600"></i>
                <span
                  v-if="unreadNotifications > 0"
                  class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                >
                  {{ unreadNotifications }}
                </span>
              </a>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown">
                <li>
                  <h6 class="dropdown-header">Notifications</h6>
                </li>
                <li v-if="messages.length === 0">
                  <a class="dropdown-item">No notification available</a>
                </li>
                <li v-for="(notification, index) in messages" :key="index">
                  <a class="dropdown-item">
                    <small class="text-muted">{{ notification.timestamp }}</small>
                    <div>{{ notification.content }}</div>
                  </a>
                </li>
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
                  <h6 class="mb-0 text-gray-600">{{ userInfo.name || 'Người dùng' }}</h6>
                  <p class="mb-0 text-sm text-gray-600">{{ userInfo.role || 'Nhân viên' }}</p>
                </div>
                <div class="user-img d-flex align-items-center">
                  <div class="avatar avatar-md">
                    <img :src="userInfo.avatar || '/src/assets/images/faces/1.jpg'" />
                  </div>
                </div>
              </div>
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
              <li>
                <h6 class="dropdown-header">Hello, {{ userInfo.name || 'Người dùng' }}!</h6>
              </li>
              <li>
                <a class="dropdown-item"><i class="icon-mid bi bi-person me-2"></i> My Profile</a>
              </li>
              <li>
                <a class="dropdown-item"><i class="icon-mid bi bi-gear me-2"></i> Settings</a>
              </li>
              <li>
                <a class="dropdown-item"><i class="icon-mid bi bi-wallet me-2"></i> Wallet</a>
              </li>
              <li>
                <hr class="dropdown-divider" />
              </li>
              <li>
                <a class="dropdown-item" @click="logout"
                  ><i class="icon-mid bi bi-box-arrow-left me-2"></i> Logout</a
                >
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
  name: 'StaffServiceHeader',
  props: {
    userInfo: {
      type: Object,
      default: () => ({
        name: '',
        role: '',
        avatar: '',
      }),
    },
  },

  data() {
    return {
      notifications: [],
      socket: null,
      retryCount: 0,
      maxRetries: 3,
      retryDelay: 3000,
      messages: [],
      unreadNotifications: 0,
    }
  },
  computed: {
    // Xóa unreadNotifications ở đây vì đã nhận qua props
  },

  methods: {
    logout() {
      this.$emit('logout')
    },
    markNotificationsAsRead() {
      this.unreadNotifications = 0
      // Có thể thêm logic để đánh dấu thông báo đã đọc ở đây
    },
  },
}
</script>
  
<style scoped>
a {
  text-decoration: none;
  cursor: pointer;
}

.badge {
  font-size: 0.7rem;
  padding: 0.25em 0.6em;
}
</style> 