<template>
  <div
    class="modal fade"
    id="addModal"
    tabindex="-1"
    aria-labelledby="addModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="addModalLabel">Thêm món ăn mới</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="$emit('add-item', newItem, selectedImage)">
            <div class="mb-3">
              <label for="addName" class="form-label">Tên món</label>
              <input
                type="text"
                class="form-control"
                id="addName"
                v-model="newItem.name"
                required
              />
            </div>
            <div class="mb-3">
              <label for="addPrice" class="form-label">Giá</label>
              <input
                type="number"
                class="form-control"
                id="addPrice"
                v-model="newItem.price"
                required
              />
            </div>
            <div class="mb-3">
              <label for="addDescription" class="form-label">Mô tả</label>
              <textarea
                class="form-control"
                id="addDescription"
                v-model="newItem.description"
                rows="3"
              ></textarea>
            </div>
            <div class="mb-3">
              <label for="addCategory" class="form-label">Danh mục</label>
              <select class="form-select" id="addCategory" v-model="newItem.categoryId" required>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <label for="addImage" class="form-label">Hình ảnh</label>
              <input
                type="file"
                class="form-control"
                id="addImage"
                accept="image/*"
                multiple
                @change="handleImageChange"
                required
              />
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
              <button type="submit" class="btn btn-primary">Thêm món</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap'

export default {
  name: 'AddProductModal',
  props: {
    categories: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      newItem: {
        name: '',
        price: 0,
        description: '',
        categoryId: this.categories.length > 0 ? this.categories[0].id : null,
      },
      selectedImage: null,
      modal: null,
    }
  },
  methods: {
    handleImageChange(event) {
      const files = event.target.files
      if (files && files.length > 0) {
        this.selectedImage = files
      }
    },
    show() {
      this.modal.show()
    },
    hide() {
      this.modal.hide()
    },
  },
  mounted() {
    this.modal = new Modal(document.getElementById('addModal'))
  },
}
</script> 