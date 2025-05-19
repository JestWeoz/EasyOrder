<template>
  <div class="container-fluid admin-container p-0">
    <AdminSidebar @toggle-sidebar="toggleSidebar" :active-component="currentRouteName" />
    <div id="main" class="layout-navbar">
      <AdminHeader @toggle-sidebar="toggleSidebar" @logout="logout" :userInfo="userInfo" />
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import AdminSidebar from '@/components/admin/AdminSidebar.vue'
import AdminHeader from '@/components/admin/AdminHeader.vue'
import { useRoute } from 'vue-router'
import { disconnectWebSocket } from '@/utils/websocket'
import axios from 'axios'
import { jwtDecode } from 'jwt-decode'

export default {
  name: 'AdminView',
  components: {
    AdminSidebar,
    AdminHeader,
  },
  data() {
    return {
      userInfo: {
        name: '',
        role: '',
        avatar: '',
      },
    }
  },
  setup() {
    const route = useRoute()
    return { route }
  },
  computed: {
    currentRouteName() {
      return this.route.name
    },
    pageTitle() {
      return  'Trang quản trị'
    },
  },
  watch: {
    pageTitle: {
      immediate: true,
      handler(newTitle) {
        document.title = newTitle
      },
    },
  },
  created() {
    this.getUserInfo()
  },
  mounted() {
    this.initializeScripts()
    document.title = this.pageTitle
  },
  methods: {
    initializeScripts() {
      // Khởi tạo Perfect Scrollbar
      if (typeof PerfectScrollbar !== 'undefined') {
        new PerfectScrollbar('.sidebar-wrapper')
      }
    },
    getUserInfo() {
      const token = localStorage.getItem('token')
      if (!token) {
        this.$router.push('/login')
        return
      }

      try {
        const decodedToken = jwtDecode(token)
        console.log('Thông tin từ token:', decodedToken)

        axios
          .get('http://localhost:8081/user/getInfo', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              userName: decodedToken.sub,
            },
          })
          .then((response) => {
            console.log('Thông tin người dùng:', response.data)
            const userData = response.data.result
            this.userInfo = {
              name:
                userData.firstName === null
                  ? 'Admin'
                  : userData.firstName + ' ' + userData.lastName,
              role:
                userData.roles[0].name ||
                (Array.isArray(decodedToken.scope)
                  ? decodedToken.scope.join(', ')
                  : decodedToken.scope || 'Admin'),
              avatar: userData.avatar || '/src/assets/images/faces/1.jpg',
            }
          })
          .catch((error) => {
            console.error('Lỗi khi lấy thông tin người dùng:', error)
            this.userInfo = {
              name: decodedToken.sub || 'Người dùng',
              role: Array.isArray(decodedToken.scope)
                ? decodedToken.scope.join(', ')
                : decodedToken.scope || 'Admin',
              avatar: '/src/assets/images/faces/1.jpg',
            }
          })
      } catch (error) {
        console.error('Lỗi khi giải mã token:', error)
        this.$router.push('/login')
      }
    },
    toggleSidebar() {
      const sidebar = document.getElementById('sidebar')
      sidebar.classList.toggle('active')
    },
    logout() {
      disconnectWebSocket()
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
  },
}
</script>

<style>
/* Import các styles cần thiết */

.admin-container {
  min-height: 100vh;
  padding: 0;
  margin: 0;
  display: flex;
}

#main {
  flex: 1;
  min-height: 100vh;
  background-color: #f2f7ff;
  position: relative;
  padding: 0;
}

/* Các styles tùy chỉnh nếu cần */
.sidebar-item.has-sub .submenu {
  display: none;
}

.sidebar-item.has-sub.active .submenu {
  display: block;
}
</style>