<template>
  <div class="page-heading">
    <div class="page-title">
      <div class="row">
        <div class="col-12 col-md-6 order-md-1 order-last">
          <h3>Quản lý bàn</h3>
          <p class="text-subtitle text-muted">Quản lý bàn trong cửa hàng.</p>
        </div>
      </div>
    </div>
    <section class="section">
      <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
          <div>Danh sách bàn</div>
          <button class="btn btn-primary" @click="showAddTableModal">
            <i class="bi bi-plus-circle me-1"></i>Thêm bàn
          </button>
        </div>
        <div class="card-body">
          <table class="table table-striped" id="table1">
            <thead>
              <tr>
                <th>Tên bàn</th>
                <th>Mô tả</th>
                <th>Số lượng người</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="table in tables" :key="table.id">
                <td>{{ table.name }}</td>
                <td>{{ table.description }}</td>
                <td>{{ table.capacity }}</td>
                <td>
                  <span :class="['badge', table.status === 1 ? 'bg-success' : 'bg-danger']">
                    {{ table.status === 1 ? 'Trống' : 'Đang sử dụng' }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-success btn-sm me-2" @click="showQR(table.id)">
                    Lấy QR
                  </button>
                  <button class="btn btn-primary btn-sm me-2">Sửa</button>
                  <button class="btn btn-danger btn-sm" @click="deleteTable(table.id)">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <!-- Modal thêm bàn mới -->
    <div
      class="modal fade"
      id="addTableModal"
      tabindex="-1"
      aria-labelledby="addTableModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addTableModalLabel">Thêm bàn mới</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="addTable">
              <div class="mb-3">
                <label for="tableName" class="form-label">Tên bàn</label>
                <input
                  type="text"
                  class="form-control"
                  id="tableName"
                  v-model="newTable.name"
                  required
                />
              </div>
              <div class="mb-3">
                <label for="tableDescription" class="form-label">Mô tả</label>
                <input
                  type="text"
                  class="form-control"
                  id="tableDescription"
                  v-model="newTable.description"
                />
              </div>
              <div class="mb-3">
                <label for="tableCapacity" class="form-label">Số lượng người</label>
                <input
                  type="number"
                  class="form-control"
                  id="tableCapacity"
                  v-model="newTable.capacity"
                  required
                  min="1"
                />
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
            <button type="button" class="btn btn-primary" @click="addTable">Thêm</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal hiển thị QR code -->
    <div
      class="modal fade"
      id="qrModal"
      tabindex="-1"
      aria-labelledby="qrModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="qrModalLabel">QR Code Bàn {{ selectedTable }}</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body text-center">
            <VueQrcode
              :value="`${tables.find((t) => t.id === selectedTable)?.url}`"
              :options="{ width: 300 }"
              ref="qrCode"
            />
            <p class="mt-3">{{ tables.find((t) => t.id === selectedTable)?.url }}</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-success" @click="downloadQR">Tải về</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VueQrcode from '@chenfengyuan/vue-qrcode'
import { Modal } from 'bootstrap'
import axios from 'axios'

export default {
  name: 'TableManagement',
  components: {
    VueQrcode,
  },
  data() {
    return {
      window: window,
      selectedTable: null,
      qrModal: null,
      addTableModal: null,
      tables: [],
      newTable: {
        name: '',
        description: '',
        capacity: 1,
      },
    }
  },
  mounted() {
    this.qrModal = new Modal(document.getElementById('qrModal'))
    this.addTableModal = new Modal(document.getElementById('addTableModal'))
    this.fetchTables()
  },
  methods: {
    async fetchTables() {
      try {
        const response = await axios.get('http://localhost:8081/table/getAll', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })
        this.tables = response.data.result.map((table) => ({
          ...table,
        }))
        console.log(this.tables)
      } catch (error) {
        console.error('Lỗi khi lấy danh sách bàn:', error)
      }
    },
    showQR(tableId) {
      this.selectedTable = tableId
      this.qrModal.show()
    },
    downloadQR() {
      const qrCodeElement = this.$refs.qrCode.$el
      const canvas = qrCodeElement.querySelector('canvas')
      const link = document.createElement('a')
      link.download = `QR-Ban-${this.selectedTable}.png`
      link.href = canvas.toDataURL('image/png')
      link.click()
    },
    showAddTableModal() {
      this.newTable = {
        name: '',
        description: '',
        capacity: 1,
      }
      this.addTableModal.show()
    },
    async addTable() {
      try {
        await axios.post('http://localhost:8081/table', this.newTable)
        this.fetchTables()
        this.addTableModal.hide()
      } catch (error) {
        console.error('Error adding table:', error)
      }
    },
    async deleteTable(id) {
      try {
        await axios.delete(`http://localhost:8081/table`, {
          params: {
            id: id,
          },
        })
        this.fetchTables()
      } catch (error) {
        console.error('Error deleting table:', error)
      }
    },
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
}

.btn {
  margin: 0.25rem;
}

.page-title {
  margin-bottom: 2rem;
}

.modal-body {
  margin: 1rem 0;
}

.modal-footer {
  margin-top: 1rem;
}
</style> 