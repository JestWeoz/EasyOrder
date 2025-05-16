<template>
  <div class="sidebar-overlay" v-if="isOpen" @click="close"></div>
  <div class="sidebar" :class="{ open: isOpen }">
    <div class="sidebar-header">
      <h5 class="mb-0">Easy Order xin chào quý khách</h5>
      <button class="btn p-0" @click="close">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>
    <div class="sidebar-content">
      <div class="contact-info-container">
        <h1 class="contact-title">Liên hệ</h1>
        <div class="contact-card">
          <div class="contact-item">
            <div class="icon-container">
              <i class="bi bi-telephone"></i>
            </div>
            <div class="contact-text">
              <span class="contact-label">Điện thoại</span>
              <a href="tel:0388112761" class="contact-value">0388112761</a>
            </div>
          </div>

          <div class="contact-item">
            <div class="icon-container">
              <i class="bi bi-envelope"></i>
            </div>
            <div class="contact-text">
              <span class="contact-label">Email</span>
              <a href="mailto:EasyOrderShop@gmail.com" class="contact-value"
                >EasyOrderShop@gmail.com</a
              >
            </div>
          </div>

          <div class="contact-item">
            <div class="icon-container">
              <i class="bi bi-geo-alt"></i>
            </div>
            <div class="contact-text">
              <span class="contact-label">Địa chỉ</span>
              <span class="contact-value">11 ngõ 7, Yên Phúc, Hà Đông, Hà Nội</span>
            </div>
          </div>
        </div>
      </div>

      <div class="social-links">
        <h6 class="text-center mb-3">Kết nối với chúng tôi</h6>
        <div class="social-media-grid">
          <a
            href="https://www.facebook.com/nguyen.quoc.tuan.281542/"
            target="_blank"
            class="social-btn facebook"
          >
            <i class="bi bi-facebook"></i>
          </a>
          <a
            href="https://www.instagram.com/nguyenquoctuan281542/"
            target="_blank"
            class="social-btn instagram"
          >
            <i class="bi bi-instagram"></i>
          </a>
          <a href="https://zalo.me/0388112760" target="_blank" class="social-btn zalo">
            <i class="bi bi-chat-dots-fill"></i>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SidebarMenu',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['update:isOpen', 'call-staff', 'request-checkout'],
  methods: {
    close() {
      this.$emit('update:isOpen', false)
    },
    callStaff() {
      this.$emit('call-staff')
      this.close()
    },
    requestCheckout() {
      this.$emit('request-checkout')
      this.close()
    },
  },
}
</script>

<style scoped>
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 2000;
}

.sidebar {
  position: fixed;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 2001;
  transition: left 0.6s ease;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.sidebar.open {
  left: 0;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.sidebar-header button {
  border: 1px #ddd;
  padding: 4px 8px;
}

.sidebar-content {
  padding: 16px;
}

.contact-info-container {
  margin-bottom: 24px;
}

.contact-title {
  text-align: center;
  margin-bottom: 16px;
  color: #333;
  font-weight: 600;
  position: relative;
  padding-bottom: 8px;
}

.contact-title:after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background-color: #1877f2;
  border-radius: 3px;
}

.contact-card {
  background-color: #f5f5f5;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.contact-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.contact-item:hover {
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.icon-container {
  background-color: #e6f4ff;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 12px;
  color: #1877f2;
  transition: all 0.3s ease;
}

.contact-item:hover .icon-container {
  background-color: #1877f2;
  color: white;
  transform: scale(1.1);
}

.contact-text {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.contact-label {
  font-weight: bold;
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.contact-value {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.contact-item:hover .contact-value {
  color: #1877f2;
}

.action-buttons {
  padding: 16px 0;
}

.staff-button {
  background-color: #e6f4ff;
  color: #1877f2;
  border: 1px solid #1877f2;
  font-weight: bold;
}

.checkout-button {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  font-weight: bold;
}

.social-links {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}

.social-links h6 {
  margin-bottom: 20px;
  font-weight: 600;
  color: #333;
}

.social-media-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  justify-content: center;
  max-width: 180px;
  margin: 0 auto;
  grid-gap: 12px;
}

.social-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  text-decoration: none;
  margin: 0 auto;
  transition: all 0.3s;
}

.social-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.facebook {
  background-color: #1877f2;
}

.instagram {
  background: linear-gradient(
    45deg,
    #f09433 0%,
    #e6683c 25%,
    #dc2743 50%,
    #cc2366 75%,
    #bc1888 100%
  );
}

.zalo {
  background-color: #0068ff;
}

.bi {
  font-size: 18px;
  display: flex;
  justify-content: center;
  align-items: center;
  line-height: 1;
}
</style> 