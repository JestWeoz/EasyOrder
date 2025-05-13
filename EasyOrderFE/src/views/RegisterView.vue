<template>
  <div id="auth">
    <div class="row h-100">
      <div class="col-lg-5 col-12">
        <div id="auth-left">
          <div class="auth-logo">
            <router-link to="/"><img src="@/assets/images/logo/logo.png" alt="Logo" /></router-link>
          </div>
          <h1 class="auth-title">Đăng Ký</h1>
          <p class="auth-subtitle mb-5">Nhập thông tin để đăng ký tài khoản.</p>

          <form @submit.prevent="handleSubmit">
            <div class="form-group position-relative has-icon-left mb-4">
              <input
                v-model="form.email"
                type="email"
                class="form-control form-control-xl"
                placeholder="Email"
                required
                autocomplete="email"
              />
              <div class="form-control-icon">
                <i class="bi bi-envelope"></i>
              </div>
            </div>
            <div class="form-group position-relative has-icon-left mb-4">
              <input
                v-model="form.username"
                type="text"
                class="form-control form-control-xl"
                placeholder="Tên đăng nhập"
                required
                autocomplete="username"
              />
              <div class="form-control-icon">
                <i class="bi bi-person"></i>
              </div>
            </div>
            <div class="form-group position-relative has-icon-left mb-4">
              <input
                v-model="form.password"
                type="password"
                class="form-control form-control-xl"
                placeholder="Mật khẩu"
                required
                autocomplete="new-password"
              />
              <div class="form-control-icon">
                <i class="bi bi-shield-lock"></i>
              </div>
            </div>
            <div class="form-group position-relative has-icon-left mb-4">
              <input
                v-model="form.confirmPassword"
                type="password"
                class="form-control form-control-xl"
                placeholder="Xác nhận mật khẩu"
                required
                autocomplete="new-password"
              />
              <div class="form-control-icon">
                <i class="bi bi-shield-lock"></i>
              </div>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg shadow-lg mt-5">
              Đăng Ký
            </button>
          </form>
          <div class="text-center mt-5 text-lg fs-4">
            <p class="text-gray-600">
              Đã có tài khoản? <router-link to="/login" class="font-bold">Đăng nhập</router-link>
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
export default {
  name: 'RegisterView',
  data() {
    return {
      form: {
        email: '',
        username: '',
        password: '',
        confirmPassword: '',
      },
    }
  },
  methods: {
    async handleSubmit() {
      if (this.form.password !== this.form.confirmPassword) {
        alert('Mật khẩu xác nhận không khớp!')
        return
      }
      await axios
        .post('http://localhost:8081/user/register', this.form)
        .then((response) => {
          console.log(response)
        })
        .catch((error) => {
          console.log(error)
        })
      this.$router.push('/login')
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
