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
        <div class="card-header">Danh sách bàn</div>
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
              <tr>
                <td>Bàn 1</td>
                <td>Bàn 4 người</td>
                <td>4</td>
                <td>
                  <span class="badge bg-success">Trống</span>
                </td>
                <td>
                  <button class="btn btn-success btn-sm me-2" @click="showQR(1)">Lấy QR</button>
                  <button class="btn btn-primary btn-sm me-2">Sửa</button>
                  <button class="btn btn-danger btn-sm">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

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
              :value="`${window.location.origin}/order/table/${selectedTable}`"
              :options="{ width: 200 }"
              ref="qrCode"
            />
            <p class="mt-3">Quét mã QR để đặt hàng cho bàn {{ selectedTable }}</p>
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
    }
  },
  mounted() {
    this.qrModal = new Modal(document.getElementById('qrModal'))
  },
  methods: {
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