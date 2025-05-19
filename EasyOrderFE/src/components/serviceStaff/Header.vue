<template>
  <header class="mb-3">
    <!-- Toast Notification -->
    <div v-if="showToast" class="custom-toast">
      <div class="toast-content">
        <strong>Thông báo mới</strong>
        <div>{{ toastMessage }}</div>
      </div>
    </div>
    <!-- End Toast Notification -->
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
                  <hr class="dropdown-divider" />
                </li>
                <li>
                  <a class="dropdown-item" @click="logout">
                    <i class="icon-mid bi bi-box-arrow-left me-2"></i> Logout
                  </a>
                </li>
              </ul>
            </div>
          </ul>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import { connectWebSocket, subscribe, disconnectWebSocket } from '@/utils/websocket'

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
    messages: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      showToast: false,
      toastMessage: '',
    }
  },

  watch: {
    messages: {
      handler(newMessages) {
        if (newMessages && newMessages.length > 0) {
          const latestMessage = newMessages[0]
          this.toastMessage = `${latestMessage.tableId}: ${latestMessage.content}`
          this.showToast = true

          // Tự động ẩn toast sau 3 giây
          setTimeout(() => {
            this.showToast = false
          }, 3000)
        }
      },
      deep: true,
      immediate: true,
    },
  },

  methods: {
    logout() {
      this.$emit('logout')
    },
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

.badge {
  font-size: 0.7rem;
  padding: 0.25em 0.6em;
}

.notification-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.notification-content {
  flex: 1;
}

.dropdown-item {
  padding: 0.75rem 1rem;
  white-space: normal;
}

.custom-toast {
  position: fixed;
  top: 20px;
  right: 40px;
  z-index: 9999;
  background: #fff;
  border: 1px solid #007bff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  padding: 16px 24px;
  border-radius: 8px;
  min-width: 260px;
  color: #333;
  animation: slideInRight 0.4s;
}
@keyframes slideInRight {
  from {
    right: -300px;
    opacity: 0;
  }
  to {
    right: 40px;
    opacity: 1;
  }
}
.toast-content strong {
  color: #007bff;
  font-size: 1rem;
}
.toast-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style> 