<template>
  <div class="page-heading">
    <div class="page-title">
      <div class="row">
        <div class="col-12 col-md-6 order-md-1 order-last">
          <h3>Quản lý doanh thu</h3>
          <p class="text-subtitle text-muted">Theo dõi và phân tích doanh thu.</p>
        </div>
      </div>
    </div>
    <section class="section">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title">Thống kê doanh thu</h4>
            </div>
            <div class="card-body">
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label for="startDate">Từ ngày</label>
                    <input type="date" class="form-control" id="startDate" v-model="startDate" />
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label for="endDate">Đến ngày</label>
                    <input type="date" class="form-control" id="endDate" v-model="endDate" />
                  </div>
                </div>
              </div>
              <div class="row mt-4">
                <div class="col-md-4">
                  <div class="card bg-primary text-white">
                    <div class="card-body">
                      <h5 class="card-title">Tổng doanh thu</h5>
                      <h3 class="card-text">{{ formatPrice(totalRevenue) }}</h3>
                    </div>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="card bg-success text-white">
                    <div class="card-body">
                      <h5 class="card-title">Số đơn hàng</h5>
                      <h3 class="card-text">{{ totalOrders }}</h3>
                    </div>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="card bg-info text-white">
                    <div class="card-body">
                      <h5 class="card-title">Đơn hàng trung bình</h5>
                      <h3 class="card-text">{{ formatPrice(averageOrder) }}</h3>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mt-4">
                <div class="col-12">
                  <div class="card">
                    <div class="card-header">
                      <h5 class="card-title">Chi tiết doanh thu</h5>
                    </div>
                    <div class="card-body">
                      <div class="table-responsive">
                        <table class="table table-striped">
                          <thead>
                            <tr>
                              <th>Ngày</th>
                              <th>Số đơn hàng</th>
                              <th>Doanh thu</th>
                              <th>Đơn hàng trung bình</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr v-for="(item, index) in revenueDetails" :key="index">
                              <td>{{ formatDate(item.date) }}</td>
                              <td>{{ item.orders }}</td>
                              <td>{{ formatPrice(item.revenue) }}</td>
                              <td>{{ formatPrice(item.average) }}</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Revenue',
  data() {
    return {
      startDate: '',
      endDate: '',
      totalRevenue: 0,
      totalOrders: 0,
      averageOrder: 0,
      revenueDetails: [],
    }
  },
  methods: {
    formatPrice(price) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
      }).format(price)
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('vi-VN')
    },
    async fetchRevenueData() {
      try {
        // Lấy danh sách đơn hàng
        const response = await axios.get('http://localhost:8081/order/getAll', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        })

        if (response.data.code === 1000) {
          const orders = response.data.result || []

          // Lọc các đơn hàng đã hoàn thành trong khoảng thời gian
          const completedOrders = orders.filter((order) => {
            const orderDate = new Date(order.created_at)
            const start = new Date(this.startDate)
            const end = new Date(this.endDate)
            end.setHours(23, 59, 59, 999) // Đặt thời gian kết thúc là cuối ngày

            return order.status === 'COMPLETED' && orderDate >= start && orderDate <= end
          })

          // Tính toán tổng doanh thu
          this.totalRevenue = completedOrders.reduce(
            (sum, order) => sum + (order.totalAmount || 0),
            0
          )

          // Tính số đơn hàng
          this.totalOrders = completedOrders.length

          // Tính đơn hàng trung bình
          this.averageOrder = this.totalOrders > 0 ? this.totalRevenue / this.totalOrders : 0

          // Tính toán chi tiết doanh thu theo ngày
          const revenueByDate = {}
          completedOrders.forEach((order) => {
            const date = new Date(order.created_at).toISOString().split('T')[0]
            if (!revenueByDate[date]) {
              revenueByDate[date] = {
                orders: 0,
                revenue: 0,
              }
            }
            revenueByDate[date].orders++
            revenueByDate[date].revenue += order.totalAmount || 0
          })

          // Chuyển đổi dữ liệu thành mảng và sắp xếp theo ngày
          this.revenueDetails = Object.entries(revenueByDate)
            .map(([date, data]) => ({
              date,
              orders: data.orders,
              revenue: data.revenue,
              average: data.orders > 0 ? data.revenue / data.orders : 0,
            }))
            .sort((a, b) => new Date(a.date) - new Date(b.date))
        }
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu doanh thu:', error)
      }
    },
  },
  watch: {
    startDate() {
      this.fetchRevenueData()
    },
    endDate() {
      this.fetchRevenueData()
    },
  },
  mounted() {
    // Set default date range to current month
    const today = new Date()
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1)
    const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0)

    this.startDate = firstDay.toISOString().split('T')[0]
    this.endDate = lastDay.toISOString().split('T')[0]

    this.fetchRevenueData()
  },
}
</script>

<style scoped>
.card {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.table-responsive {
  margin-top: 1rem;
}
</style> 