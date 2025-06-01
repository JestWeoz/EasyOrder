<template>
  <div id="auth">
    <div class="row h-100">
      <div class="col-lg-5 col-12">
        <div id="auth-left">
          <div class="auth-logo">
            <a href="/menu"
              ><img src="@/assets/images/logo/logo.png" alt="Logo" class="logo-img"
            /></a>
          </div>
          <h1 class="auth-title">Đăng nhập.</h1>
          <p class="auth-subtitle mb-5">Vui lòng đăng nhập để tiếp tục</p>

          <form @submit.prevent="handleLogin">
            <div class="form-group position-relative has-icon-left mb-4">
              <input
                type="text"
                class="form-control form-control-xl"
                placeholder="Tên đăng nhập"
                v-model="username"
                autocomplete="username"
              />
              <div class="form-control-icon">
                <i class="bi bi-person"></i>
              </div>
            </div>
            <div class="form-group position-relative has-icon-left mb-4">
              <input
                type="password"
                class="form-control form-control-xl"
                placeholder="Mật khẩu"
                v-model="password"
                autocomplete="current-password"
              />
              <div class="form-control-icon">
                <i class="bi bi-shield-lock"></i>
              </div>
            </div>
            <div class="form-check form-check-lg d-flex align-items-end">
              <input
                class="form-check-input me-2"
                type="checkbox"
                v-model="rememberMe"
                id="flexCheckDefault"
              />
              <label class="form-check-label text-gray-600" for="flexCheckDefault">
                Ghi nhớ đăng nhập
              </label>
            </div>
            <button
              type="submit"
              class="btn btn-primary btn-block btn-lg shadow-lg mt-5"
              :disabled="isLoginDisabled"
            >
              {{ loginButtonText }}
            </button>
          </form>
        </div>
      </div>
      <div class="col-lg-7 d-none d-lg-block">
        <div id="auth-right"></div>
      </div>
    </div>
    <div v-if="showErrorModal" class="modal-backdrop">
      <div class="modal-content">
        <h5>Lỗi đăng nhập</h5>
        <p>{{ errorMessage }}</p>
        <button class="btn btn-primary" @click="showErrorModal = false">Đóng</button>
      </div>
    </div>
  </div>
</template>
  
<script>
import axios from 'axios'
import { jwtDecode } from 'jwt-decode'

const MAX_LOGIN_ATTEMPTS = 5
const LOCKOUT_DURATION = 5 * 60 * 1000 // 5 phút tính bằng milliseconds

export default {
  name: 'LoginView',
  data() {
    return {
      username: '',
      password: '',
      rememberMe: false,
      showErrorModal: false,
      errorMessage: '',
      loginAttempts: 0,
      lockoutEndTime: null,
      countdownInterval: null,
    }
  },
  computed: {
    isLoginDisabled() {
      return this.lockoutEndTime && Date.now() < this.lockoutEndTime
    },
    loginButtonText() {
      if (this.isLoginDisabled) {
        const remainingTime = Math.ceil((this.lockoutEndTime - Date.now()) / 1000)
        return `Vui lòng đợi ${remainingTime} giây`
      }
      return 'Đăng nhập'
    },
  },
  created() {
    // Khôi phục trạng thái từ localStorage
    const savedAttempts = localStorage.getItem('loginAttempts')
    const savedLockoutTime = localStorage.getItem('lockoutEndTime')

    if (savedAttempts) {
      this.loginAttempts = parseInt(savedAttempts)
    }
    if (savedLockoutTime) {
      this.lockoutEndTime = parseInt(savedLockoutTime)
      if (Date.now() < this.lockoutEndTime) {
        this.startCountdown()
      } else {
        this.resetLoginAttempts()
      }
    }
  },
  beforeDestroy() {
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval)
    }
  },
  methods: {
    startCountdown() {
      if (this.countdownInterval) {
        clearInterval(this.countdownInterval)
      }
      this.countdownInterval = setInterval(() => {
        if (Date.now() >= this.lockoutEndTime) {
          clearInterval(this.countdownInterval)
          this.resetLoginAttempts()
        }
      }, 1000)
    },
    resetLoginAttempts() {
      this.loginAttempts = 0
      this.lockoutEndTime = null
      localStorage.removeItem('loginAttempts')
      localStorage.removeItem('lockoutEndTime')
    },
    handleLogin() {
      if (this.isLoginDisabled) {
        return
      }

      axios
        .post('http://localhost:8081/auth/token', {
          username: this.username,
          password: this.password,
        })
        .then((response) => {
          const token = response.data.result.token
          const decodedToken = jwtDecode(token)
          console.log(decodedToken)
          localStorage.setItem('token', token)
          this.resetLoginAttempts() // Reset sau khi đăng nhập thành công

          if (decodedToken.scope.includes('ADMIN')) {
            this.$router.push('/admin')
          } else if (decodedToken.scope.includes('STAFF')) {
            this.$router.push('/staff/dashboard')
          }
          console.log('Login successful:', response.data)
        })
        .catch((error) => {
          this.loginAttempts++
          localStorage.setItem('loginAttempts', this.loginAttempts.toString())

          if (this.loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            this.lockoutEndTime = Date.now() + LOCKOUT_DURATION
            localStorage.setItem('lockoutEndTime', this.lockoutEndTime.toString())
            this.startCountdown()
          }

          this.showErrorModal = true
          this.errorMessage = `Đăng nhập thất bại. Còn ${
            MAX_LOGIN_ATTEMPTS - this.loginAttempts
          } lần thử.`
        })
    },
  },
}
</script>
  
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap');
@import '@/assets/css/bootstrap.css';
@import '@/assets/vendors/bootstrap-icons/bootstrap-icons.css';
@import '@/assets/css/app.css';
@import '@/assets/css/pages/auth.css';
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal-content {
  background: #fff;
  padding: 24px 32px;
  border-radius: 12px;
  min-width: 320px;
  max-width: 50vw;
  min-height: 180px;
  max-height: 50vh;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15);
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
  