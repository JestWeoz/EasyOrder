<template>
  <div
    class="modal fade"
    id="editModal"
    tabindex="-1"
    aria-labelledby="editModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editModalLabel">Chỉnh sửa món ăn</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="$emit('update-item', editingItem, selectedImage)">
            <div class="mb-3">
              <label for="editName" class="form-label">Tên món</label>
              <input
                type="text"
                class="form-control"
                id="editName"
                v-model="editingItem.name"
                required
              />
            </div>
            <div class="mb-3">
              <label for="editPrice" class="form-label">Giá</label>
              <input
                type="number"
                class="form-control"
                id="editPrice"
                v-model="editingItem.price"
                required
              />
            </div>
            <div class="mb-3">
              <label for="editDescription" class="form-label">Mô tả</label>
              <textarea
                class="form-control"
                id="editDescription"
                v-model="editingItem.description"
                rows="3"
              ></textarea>
            </div>
            <div class="mb-3">
              <label for="editCategory" class="form-label">Danh mục</label>
              <select
                class="form-select"
                id="editCategory"
                v-model="editingItem.categoryId"
                required
              >
                <option
                  v-for="category in categories"
                  :key="category.id"
                  :value="category.id"
                  :selected="category.id === editingItem.categoryId"
                >
                  {{ category.name }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <label for="editImage" class="form-label">Hình ảnh</label>
              <div class="d-flex align-items-center">
                <img
                  v-if="editingItem.images && editingItem.images.length > 0"
                  :src="editingItem.images[0].url"
                  alt="Hình ảnh món ăn"
                  class="preview-image me-3"
                />
                <input
                  type="file"
                  class="form-control"
                  id="editImage"
                  accept="image/*"
                  multiple
                  @change="handleImageChange"
                />
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
              <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
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
  name: 'EditProductModal',
  props: {
    categories: {
      type: Array,
      required: true,
    },
    item: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      editingItem: {
        id: null,
        name: '',
        price: 0,
        description: '',
        categoryId: null,
        images: [],
      },
      selectedImage: null,
      modal: null,
    }
  },
  watch: {
    item: {
      immediate: true,
      handler(newVal) {
        this.editingItem = {
          id: newVal.id,
          name: newVal.name,
          price: newVal.price,
          description: newVal.description,
          categoryId:
            newVal.categoryId || (this.categories.length > 0 ? this.categories[0].id : null),
          images: newVal.images || [],
        }
        this.selectedImage = null
      },
    },
  },
  methods: {
    handleImageChange(event) {
      const files = event.target.files
      if (files && files.length > 0) {
        this.selectedImage = files
        // Tạo URL tạm thời để hiển thị ảnh preview
        const reader = new FileReader()
        reader.onload = (e) => {
          this.editingItem.images = [{ url: e.target.result }]
        }
        reader.readAsDataURL(files[0])
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
    this.modal = new Modal(document.getElementById('editModal'))
  },
}
</script>

<style scoped>
.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}
</style> 