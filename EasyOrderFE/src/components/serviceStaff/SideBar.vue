<template>
  <div id="sidebar" class="active">
    <div class="sidebar-wrapper active">
      <div class="sidebar-header">
        <div class="d-flex justify-content-between">
          <div>
            <a><img src="/src/assets/images/logo/logo.png" style="width: 230px; height: 70px" /></a>
          </div>
          <div class="toggler">
            <a class="sidebar-hide d-xl-none d-block" @click="$emit('toggle-sidebar')"
              ><i class="bi bi-x bi-middle"></i
            ></a>
          </div>
        </div>
      </div>
      <div class="sidebar-menu">
        <ul class="menu">
          <li class="sidebar-title">Menu</li>

          <li class="sidebar-item" :class="{ active: activeComponent === 'Dashboard' }">
            <a class="sidebar-link" @click="changeComponent('Dashboard')">
              <i class="bi bi-grid-fill"></i>
              <span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-item" :class="{ active: activeComponent === 'Orders' }">
            <a class="sidebar-link" @click="changeComponent('Orders')">
              <i class="bi bi-grid-1x2-fill"></i>
              <span>Đơn hàng</span>
            </a>
          </li>
          <li class="sidebar-item" :class="{ active: activeComponent === 'KitchenManager' }">
            <a class="sidebar-link" @click="changeComponent('KitchenManager')">
              <i class="bi bi-grid-1x2-fill"></i>
              <span>Bếp</span>
            </a>
          </li>
        </ul>
      </div>
      <button class="sidebar-toggler btn x"><i data-feather="x"></i></button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ServiceStaffSideBar',
  props: {
    activeComponent: {
      type: String,
      default: 'TableManagement',
    },
  },
  methods: {
    toggleSubmenu(event) {
      event.preventDefault()
      const parent = event.currentTarget.parentElement
      const submenu = parent.querySelector('.submenu')

      if (submenu) {
        submenu.classList.toggle('active')
        parent.classList.toggle('active')
      }
    },
    changeComponent(componentName) {
      this.$emit('change-component', componentName)

      // Chuyển router dựa trên component được chọn
      switch (componentName) {
        case 'Dashboard':
          // Chuyển đến trang chủ/dashboard
          this.$router.push('/service-staff/dashboard')
          break
        case 'Orders':
          // Chuyển đến trang quản lý đơn hàng
          this.$router.push('/service-staff/orders')
          break
        case 'KitchenManager':
          // Chuyển đến trang quản lý bếp
          this.$router.push('/service-staff/kitchen-management')
          break
      }
    },
  },
}
</script>

<style>
.sidebar-item.has-sub .submenu {
  display: none;
  cursor: pointer;
}

.sidebar-item.has-sub.active .submenu {
  display: block;
}

.sidebar-item.active > .sidebar-link,
.submenu-item.active > a {
  background-color: #f2f7ff;
  color: #5d87ff;
  font-weight: 600;
}

.sidebar-item.active > .sidebar-link i,
.submenu-item.active > a {
  color: #5d87ff;
}

/* Thêm cursor pointer cho các phần tử có thể click */
.sidebar-link,
.submenu-item a,
.sidebar-hide {
  cursor: pointer;
}
</style> 