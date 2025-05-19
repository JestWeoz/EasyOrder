<template>
  <div class="page-heading">
    <div class="page-title">
      <div class="row">
        <div class="col-12 col-md-6 order-md-1 order-last">
          <h3>Quản lý nhân viên</h3>
          <p class="text-subtitle text-muted">Quản lý thông tin nhân viên.</p>
        </div>
      </div>
    </div>
    <section class="section">
      <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
          <span>Danh sách nhân viên</span>
          <button class="btn btn-primary" @click="handleAddStaff">Thêm nhân viên</button>
        </div>
        <div class="card-body">
          <table class="table table-striped" id="table1">
            <thead>
              <tr>
                <th>Tên nhân viên</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Vị trí</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="staff in staffs" :key="staff.username">
                <td>
                  {{ staff.firstName ? `${staff.firstName} ${staff.lastName}` : staff.username }}
                </td>
                <td>{{ staff.email || '---' }}</td>
                <td>{{ staff.phone || '---' }}</td>
                <td>
                  <span v-if="staff.roles && staff.roles.length">
                    {{ staff.roles[0].name }}
                  </span>
                  <span v-else>---</span>
                </td>
                <td>
                  <button class="btn btn-primary btn-sm" @click="handleEditStaff(staff)">
                    Sửa
                  </button>
                  <button class="btn btn-danger btn-sm">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <!-- Modal Sửa Nhân Viên -->
    <div
      class="modal fade"
      id="editStaffModal"
      tabindex="-1"
      aria-labelledby="editStaffModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editStaffModalLabel">Sửa thông tin nhân viên</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="handleUpdateStaff">
              <div class="mb-3">
                <label class="form-label">Tên đăng nhập</label>
                <input type="text" class="form-control" v-model="editStaff.username" disabled />
              </div>
              <div class="mb-3">
                <label class="form-label">Mật khẩu mới</label>
                <input
                  type="password"
                  class="form-control"
                  v-model="editStaff.password"
                  placeholder="Để trống nếu không muốn thay đổi"
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Họ</label>
                <input type="text" class="form-control" v-model="editStaff.firstName" />
              </div>
              <div class="mb-3">
                <label class="form-label">Tên</label>
                <input type="text" class="form-control" v-model="editStaff.lastName" />
              </div>
              <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" class="form-control" v-model="editStaff.email" />
              </div>
              <div class="mb-3">
                <label class="form-label">Số điện thoại</label>
                <input type="text" class="form-control" v-model="editStaff.phone" />
              </div>
              <div class="mb-3">
                <label class="form-label">Vị trí</label>
                <select class="form-select" v-model="editStaff.roles[0]">
                  <option v-for="role in allRoles" :key="role.id" :value="role.id">
                    {{ role.name }}
                  </option>
                </select>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { Modal } from 'bootstrap'

export default {
  name: 'StaffManagement',
  data() {
    return {
      staffs: [],
      showEditModal: false,
      editStaff: {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        roles: [],
      },
      allRoles: [],
      editModal: null,
    }
  },
  methods: {
    async fetchStaffs() {
      try {
        const res = await axios.get('http://localhost:8081/user/getAll', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })
        if (res.data && res.data.result) {
          this.staffs = res.data.result
        }
      } catch (err) {
        console.error('Lỗi khi lấy danh sách nhân viên:', err)
      }
    },
    async fetchRoles() {
      try {
        const res = await axios.get('http://localhost:8081/roles', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })
        if (res.data && res.data.result) {
          this.allRoles = res.data.result
        }
      } catch (err) {
        console.error('Lỗi khi lấy danh sách roles:', err)
      }
    },
    handleAddStaff() {
      this.$router.push('/register')
    },
    handleEditStaff(staff) {
      this.editStaff = {
        ...staff,
        password: '',
        roles: staff.roles ? [staff.roles[0].id] : [],
      }
      this.editModal.show()
    },
    async handleUpdateStaff() {
      try {
        const updateData = {
          ...this.editStaff,
          password: this.editStaff.password || undefined,
        }

        const res = await axios.put(
          `http://localhost:8081/user/update`,
          updateData,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          }
        )

        if (res.data && res.data.result) {
          this.editModal.hide()
          this.fetchStaffs()
          alert('Cập nhật thông tin nhân viên thành công!')
        }
      } catch (err) {
        console.error('Lỗi khi cập nhật thông tin nhân viên:', err)
        alert('Có lỗi xảy ra khi cập nhật thông tin nhân viên!')
      }
    },
  },
  mounted() {
    this.fetchStaffs()
    this.fetchRoles()
    this.editModal = new Modal(document.getElementById('editStaffModal'))
  },
}
</script>

<style>
.table {
  margin-bottom: 0;
}

.page-heading {
  margin: 1.5rem;
}

.card {
  margin-bottom: 2rem;
}

.card-header {
  margin-bottom: 1rem;
  padding: 1rem;
}

.btn {
  margin: 0.25rem;
}

.btn-primary {
  background-color: #435ebe;
  border-color: #435ebe;
}

.btn-primary:hover {
  background-color: #3950a2;
  border-color: #3950a2;
}

.page-title {
  margin-bottom: 2rem;
}

.modal-content {
  border-radius: 0.5rem;
}

.form-label {
  font-weight: 500;
}

.form-control:disabled {
  background-color: #e9ecef;
}
</style> 