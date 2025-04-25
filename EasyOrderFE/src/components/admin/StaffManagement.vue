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
        <div class="card-header">Danh sách nhân viên</div>
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
              <tr>
                <td>Nguyễn Văn A</td>
                <td>nva@example.com</td>
                <td>0123456789</td>
                <td>Nhân viên phục vụ</td>
                <td>
                  <button class="btn btn-primary btn-sm">Sửa</button>
                  <button class="btn btn-danger btn-sm">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'StaffManagement',
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
</style> 