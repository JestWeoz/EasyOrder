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
            <li class="nav-item dropdown me-1"></li>
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
                  <h6 class="mb-0 text-gray-600">Quoc Tuan</h6>
                  <p class="mb-0 text-sm text-gray-600">Administrator</p>
                </div>
                <div class="user-img d-flex align-items-center">
                  <div class="avatar avatar-md">
                    <img src="/src/assets/images/faces/1.jpg" />
                  </div>
                </div>
              </div>
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
              <li>
                <h6 class="dropdown-header">Hello, Quoc Tuan!</h6>
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
                <a class="dropdown-item"
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