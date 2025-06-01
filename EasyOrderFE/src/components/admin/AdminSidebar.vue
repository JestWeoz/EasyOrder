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

          <li class="sidebar-item" :class="{ active: activeComponent === 'MenuManagement' }">
            <a class="sidebar-link" @click="changeComponent('MenuManagement')">
              <i class="bi bi-grid-fill"></i>
              <span>Quản lý menu</span>
            </a>
          </li>
          <li class="sidebar-item" :class="{ active: activeComponent === 'TableManagement' }">
            <a class="sidebar-link" @click="changeComponent('TableManagement')">
              <i class="bi bi-grid-1x2-fill"></i>
              <span>Quản lý bàn</span>
            </a>
          </li>

          <li class="sidebar-item" :class="{ active: activeComponent === 'Revenue' }">
            <a class="sidebar-link" @click="changeComponent('Revenue')">
              <i class="bi bi-graph-up"></i>
              <span>Doanh thu</span>
            </a>
          </li>

          <li class="sidebar-item" :class="{ active: activeComponent === 'StaffManagement' }">
            <a class="sidebar-link" @click="changeComponent('StaffManagement')">
              <i class="bi bi-grid-1x2-fill"></i>
              <span>Quản lý nhân viên</span>
            </a>
          </li>
        </ul>
      </div>
      <button class="sidebar-toggler btn x"><i data-feather="x"></i></button>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  name: 'AdminSidebar',
  props: {
    activeComponent: {
      type: String,
      default: 'TableManagement',
    },
  },
  setup() {
    const router = useRouter()
    return { router }
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
        case 'MenuManagement':
          this.router.push('/admin/menu')
          break
        case 'TableManagement':
          this.router.push('/admin/tables')
          break
        case 'StaffManagement':
          this.router.push('/admin/staff')
          break
        case 'Revenue':
          this.router.push('/admin/revenue')
          break
        default:
          this.router.push('/admin')
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