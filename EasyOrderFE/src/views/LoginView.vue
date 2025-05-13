<template>
  <div id="auth">
    <div class="row h-100">
      <div class="col-lg-5 col-12">
        <div id="auth-left">
          <div class="auth-logo">
            <a href="/"><img src="@/assets/images/logo/logo.png" alt="Logo" class="logo-img" /></a>
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
            <button type="submit" class="btn btn-primary btn-block btn-lg shadow-lg mt-5">
              Đăng nhập
            </button>
          </form>
          <div class="text-center mt-5 text-lg fs-4">
            <p class="text-gray-600">
              Bạn chưa có tài khoản?
              <router-link to="/register" class="font-bold">Đăng ký</router-link>
            </p>
            <p>
              <router-link to="/forgot-password" class="font-bold">Quên mật khẩu?</router-link>
            </p>
          </div>
        </div>
      </div>
      <div class="col-lg-7 d-none d-lg-block">
        <div id="auth-right"></div>
      </div>
    </div>
  </div>
</template>
  
<script>
import axios from 'axios'
import { jwtDecode } from 'jwt-decode'
export default {
  name: 'LoginView',
  data() {
    return {
      username: '',
      password: '',
      rememberMe: false,
    }
  },
  methods: {
    handleLogin() {
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
          if (decodedToken.scope.includes('ADMIN')) {
            this.$router.push('/admin')
          } else if (decodedToken.scope.includes('STAFF')) {
            this.$router.push('/service-staff')
          } else if (decodedToken.scope.includes('CUSTOMER')) {
            this.$router.push('/')
          }
          console.log('Login successful:', response.data)
        })
        .catch((error) => {
          console.error('Login failed:', error)
          // Hiển thị thông báo lỗi
          this.$toast.error('Invalid username or password')
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
</style>
  